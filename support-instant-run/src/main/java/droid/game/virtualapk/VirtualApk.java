package droid.game.virtualapk;

import android.app.*;
import android.content.ComponentName;
import android.content.Context;
import android.content.IContentProvider;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Singleton;
import droid.game.plugin.manager.LoadedApk;
import droid.game.plugin.manager.PluginManager;
import droid.game.virtualapk.delegate.ActivityManagerProxy;
import droid.game.virtualapk.delegate.IContentProviderProxy;
import droid.game.virtualapk.delegate.RemoteContentProvider;
import droid.game.virtualapk.internal.ComponentsHandler;
import droid.game.virtualapk.internal.Constants;
import droid.game.virtualapk.internal.LoadedPlugin;
import droid.game.virtualapk.internal.VAInstrumentation;
import droid.game.virtualapk.internal.utils.PluginUtil;
import droid.game.virtualapk.utils.Reflector;
import droid.game.virtualapk.utils.RunUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VirtualApk extends PluginManager {

    public static final String TAG = Constants.TAG_PREFIX + "VirtualApk";

    private static volatile VirtualApk sInstance = null;

    // Context of host app
    protected final Context mContext;
    protected final Application mApplication;
    protected ComponentsHandler mComponentsHandler;
    protected final Map<String, LoadedPlugin> mPlugins = new ConcurrentHashMap<>();
    protected final List<Callback> mCallbacks = new ArrayList<>();

    protected VAInstrumentation mInstrumentation; // Hooked instrumentation
    protected IActivityManager mActivityManager; // Hooked IActivityManager binder
    protected IContentProvider mIContentProvider; // Hooked IContentProvider binder

    public static VirtualApk getInstance(Context base) {
        if (sInstance == null) {
            synchronized (VirtualApk.class) {
                if (sInstance == null) {
                    sInstance = createInstance(base);
                }
            }
        }

        return sInstance;
    }

    private static VirtualApk createInstance(Context context) {
        try {
            Bundle metaData = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
                    .metaData;

            if (metaData == null) {
                return new VirtualApk(context);
            }

            String factoryClass = metaData.getString("VA_FACTORY");

            if (factoryClass == null) {
                return new VirtualApk(context);
            }

            VirtualApk VirtualApk = Reflector.on(factoryClass).method("create", Context.class).call(context);

            if (VirtualApk != null) {
                Log.d(TAG, "Created a instance of " + VirtualApk.getClass());
                return VirtualApk;
            }

        } catch (Exception e) {
            Log.w(TAG, "Created the instance error!", e);
        }

        return new VirtualApk(context);
    }

    protected VirtualApk(Context context) {
        super(context);
        if (context instanceof Application) {
            this.mApplication = (Application) context;
            this.mContext = mApplication.getBaseContext();
        } else {
            final Context app = context.getApplicationContext();
            if (app == null) {
                this.mContext = context;
                this.mApplication = ActivityThread.currentApplication();
            } else {
                this.mApplication = (Application) app;
                this.mContext = mApplication.getBaseContext();
            }
        }

        mComponentsHandler = createComponentsHandler();
        //hookCurrentProcess();
    }

    protected void hookCurrentProcess() {
        hookInstrumentationAndHandler();
        hookSystemServices();
        //hookDataBindingUtil();
    }

    @Override
    public void init(Configuration configuration) {
        super.init(configuration);
        if (configuration.isDebug()) {
            Log.d(TAG, "PluginManager init");
            Log.d(TAG, configuration.toString());
        }
        if (getConfiguration().isHookSystem()) {
            hookCurrentProcess();
        }
        RunUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                doInWorkThread();
            }
        });
    }

    protected void doInWorkThread() {
    }

    @Override
    public Application getHostApplication() {
        return this.mApplication;
    }

    protected ComponentsHandler createComponentsHandler() {
        return new ComponentsHandler(this);
    }

    protected VAInstrumentation createInstrumentation(Instrumentation origin) throws Exception {
        return new VAInstrumentation(this, origin);
    }

    protected ActivityManagerProxy createActivityManagerProxy(IActivityManager origin) throws Exception {
        return new ActivityManagerProxy(this, origin);
    }

    protected LoadedPlugin createLoadedPlugin(File apk) throws Exception {
        return new LoadedPlugin(this, this.mContext, apk);
    }

    protected void hookDataBindingUtil() {
        Reflector.QuietReflector reflector = Reflector.QuietReflector.on("android.databinding.DataBindingUtil").field("sMapper");
        Object old = reflector.get();
        if (old != null) {
            try {
                Callback callback = Reflector.on("android.databinding.DataBinderMapperProxy").constructor().newInstance();
                reflector.set(callback);
                addCallback(callback);
                Log.d(TAG, "hookDataBindingUtil succeed : " + callback);
            } catch (Reflector.ReflectedException e) {
                Log.w(TAG, e);
            }
        }
    }

    @Override
    public void addCallback(Callback callback) {
        if (callback == null) {
            return;
        }
        synchronized (mCallbacks) {
            if (mCallbacks.contains(callback)) {
                throw new RuntimeException("Already added " + callback + "!");
            }
            mCallbacks.add(callback);
        }
    }

    @Override
    public void removeCallback(Callback callback) {
        synchronized (mCallbacks) {
            mCallbacks.remove(callback);
        }
    }

    /**
     * hookSystemServices, but need to compatible with Android O in future.
     */
    protected void hookSystemServices() {
        try {
            Singleton<IActivityManager> defaultSingleton;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                defaultSingleton = Reflector.on(ActivityManager.class).field("IActivityManagerSingleton").get();
            } else {
                defaultSingleton = Reflector.on(ActivityManagerNative.class).field("gDefault").get();
            }
            IActivityManager origin = defaultSingleton.get();
            IActivityManager activityManagerProxy = (IActivityManager) Proxy.newProxyInstance(mContext.getClassLoader(), new Class[] { IActivityManager.class },
                    createActivityManagerProxy(origin));

            // Hook IActivityManager from ActivityManagerNative
            Reflector.with(defaultSingleton).field("mInstance").set(activityManagerProxy);

            if (defaultSingleton.get() == activityManagerProxy) {
                this.mActivityManager = activityManagerProxy;
                Log.d(TAG, "hookSystemServices succeed : " + mActivityManager);
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    protected void hookInstrumentationAndHandler() {
        try {
            ActivityThread activityThread = ActivityThread.currentActivityThread();
            Instrumentation baseInstrumentation = activityThread.getInstrumentation();
//            if (baseInstrumentation.getClass().getName().contains("lbe")) {
//                // reject executing in paralell space, for example, lbe.
//                System.exit(0);
//            }

            final VAInstrumentation instrumentation = createInstrumentation(baseInstrumentation);

            Reflector.with(activityThread).field("mInstrumentation").set(instrumentation);
            Handler mainHandler = Reflector.with(activityThread).method("getHandler").call();
            Reflector.with(mainHandler).field("mCallback").set(instrumentation);
            this.mInstrumentation = instrumentation;
            Log.d(TAG, "hookInstrumentationAndHandler succeed : " + mInstrumentation);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    protected void hookIContentProviderAsNeeded() {
        Uri uri = Uri.parse(RemoteContentProvider.getUri(mContext));
        mContext.getContentResolver().call(uri, "wakeup", null, null);
        try {
            Field authority = null;
            Field provider = null;
            ActivityThread activityThread = ActivityThread.currentActivityThread();
            Map providerMap = Reflector.with(activityThread).field("mProviderMap").get();
            Iterator iter = providerMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                String auth;
                if (key instanceof String) {
                    auth = (String) key;
                } else {
                    if (authority == null) {
                        authority = key.getClass().getDeclaredField("authority");
                        authority.setAccessible(true);
                    }
                    auth = (String) authority.get(key);
                }
                if (auth.equals(RemoteContentProvider.getAuthority(mContext))) {
                    if (provider == null) {
                        provider = val.getClass().getDeclaredField("mProvider");
                        provider.setAccessible(true);
                    }
                    IContentProvider rawProvider = (IContentProvider) provider.get(val);
                    IContentProvider proxy = IContentProviderProxy.newInstance(mContext, rawProvider);
                    mIContentProvider = proxy;
                    Log.d(TAG, "hookIContentProvider succeed : " + mIContentProvider);
                    break;
                }
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    /**
     * load a plugin into memory, then invoke it's Application.
     * @param apk the file of plugin, should end with .apk
     * @throws Exception
     */
    @Override
    public void loadPlugin(File apk) throws Exception {
        if (null == apk) {
            throw new IllegalArgumentException("error : apk is null.");
        }

        if (!apk.exists()) {
            // throw the FileNotFoundException by opening a stream.
            InputStream in = new FileInputStream(apk);
            in.close();
        }

        LoadedPlugin plugin = createLoadedPlugin(apk);
        if (null == plugin) {
            throw new RuntimeException("Can't load plugin which is invalid: " + apk.getAbsolutePath());
        }

        this.mPlugins.put(plugin.getPackageName(), plugin);
        synchronized (mCallbacks) {
            for (int i = 0; i < mCallbacks.size(); i++) {
                mCallbacks.get(i).onAddedLoadedPlugin(plugin);
            }
        }
    }

    @Override
    public LoadedApk getLoadedApk(Intent intent) {
        return getLoadedPlugin(PluginUtil.getComponent(intent));
    }

    @Override
    public LoadedApk getLoadedApk(ComponentName component) {
        if (component == null) {
            return null;
        }
        return this.getLoadedPlugin(component.getPackageName());
    }

    @Override
    public LoadedApk getLoadedApk(String packageName) {
        return this.mPlugins.get(packageName);
    }

    @Override
    public List<LoadedApk> getAllLoadedApks() {
        List<LoadedApk> list = new ArrayList<>();
        list.addAll(mPlugins.values());
        return list;
    }

    public LoadedPlugin getLoadedPlugin(Intent intent) {
        return getLoadedPlugin(PluginUtil.getComponent(intent));
    }

    public LoadedPlugin getLoadedPlugin(ComponentName component) {
        if (component == null) {
            return null;
        }
        return this.getLoadedPlugin(component.getPackageName());
    }

    public LoadedPlugin getLoadedPlugin(String packageName) {
        return this.mPlugins.get(packageName);
    }

    public List<LoadedPlugin> getAllLoadedPlugins() {
        List<LoadedPlugin> list = new ArrayList<>();
        list.addAll(mPlugins.values());
        return list;
    }

    @Override
    public Context getHostContext() {
        return this.mContext;
    }

    public VAInstrumentation getVAInstrumentation() {
        return this.mInstrumentation;
    }

    @Override
    public Instrumentation getInstrumentation(){
        if (this.mInstrumentation != null){
            return this.mInstrumentation;
        }
        try{
            ActivityThread activityThread = ActivityThread.currentActivityThread();
            Instrumentation baseInstrumentation = activityThread.getInstrumentation();
            return baseInstrumentation;
        }catch (Exception e){

        }
        return null;
    }

    public IActivityManager getActivityManager() {
        return this.mActivityManager;
    }

    public synchronized IContentProvider getIContentProvider() {
        if (mIContentProvider == null) {
            hookIContentProviderAsNeeded();
        }

        return mIContentProvider;
    }

    public ComponentsHandler getComponentsHandler() {
        return mComponentsHandler;
    }

    public ResolveInfo resolveActivity(Intent intent) {
        return this.resolveActivity(intent, 0);
    }

    public ResolveInfo resolveActivity(Intent intent, int flags) {
        for (LoadedPlugin plugin : this.mPlugins.values()) {
            ResolveInfo resolveInfo = plugin.resolveActivity(intent, flags);
            if (null != resolveInfo) {
                return resolveInfo;
            }
        }

        return null;
    }

    public ResolveInfo resolveService(Intent intent, int flags) {
        for (LoadedPlugin plugin : this.mPlugins.values()) {
            ResolveInfo resolveInfo = plugin.resolveService(intent, flags);
            if (null != resolveInfo) {
                return resolveInfo;
            }
        }

        return null;
    }

    public ProviderInfo resolveContentProvider(String name, int flags) {
        for (LoadedPlugin plugin : this.mPlugins.values()) {
            ProviderInfo providerInfo = plugin.resolveContentProvider(name, flags);
            if (null != providerInfo) {
                return providerInfo;
            }
        }

        return null;
    }

    /**
     * used in PluginPackageManager, do not invoke it from outside.
     */
    @Deprecated
    public List<ResolveInfo> queryIntentActivities(Intent intent, int flags) {
        List<ResolveInfo> resolveInfos = new ArrayList<ResolveInfo>();

        for (LoadedPlugin plugin : this.mPlugins.values()) {
            List<ResolveInfo> result = plugin.queryIntentActivities(intent, flags);
            if (null != result && result.size() > 0) {
                resolveInfos.addAll(result);
            }
        }

        return resolveInfos;
    }

    /**
     * used in PluginPackageManager, do not invoke it from outside.
     */
    @Deprecated
    public List<ResolveInfo> queryIntentServices(Intent intent, int flags) {
        List<ResolveInfo> resolveInfos = new ArrayList<ResolveInfo>();

        for (LoadedPlugin plugin : this.mPlugins.values()) {
            List<ResolveInfo> result = plugin.queryIntentServices(intent, flags);
            if (null != result && result.size() > 0) {
                resolveInfos.addAll(result);
            }
        }

        return resolveInfos;
    }

    /**
     * used in PluginPackageManager, do not invoke it from outside.
     */
    @Deprecated
    public List<ResolveInfo> queryBroadcastReceivers(Intent intent, int flags) {
        List<ResolveInfo> resolveInfos = new ArrayList<ResolveInfo>();

        for (LoadedPlugin plugin : this.mPlugins.values()) {
            List<ResolveInfo> result = plugin.queryBroadcastReceivers(intent, flags);
            if (null != result && result.size() > 0) {
                resolveInfos.addAll(result);
            }
        }

        return resolveInfos;
    }
}
