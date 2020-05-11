package droid.game.xsdk.inner;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import droid.game.android.oaid.AndroidOaidProvider;
import droid.game.core.keep.IApplication;
import droid.game.permission.PermissionBirdge;
import droid.game.plugin.manager.FreeReflect;
import droid.game.plugin.manager.PluginManager;
import droid.game.virtualapk.VirtualApk;

public class InnerApp implements IApplication {
    @Override
    public void attachBaseContext(Application app) {
        setupVirtualApkPluginManager(app);
    }

    @Override
    public void onCreate(Application app) {
        setupMsaGetDeviceInfo(app);
        setupDynamicPermissionSupport(app);
    }

    @Override
    public void onTerminate(Application app) {
    }

    @Override
    public void onConfigurationChanged(Application app, Configuration newConfig) {
    }

    @Override
    public void onLowMemory(Application app) {
    }

    @Override
    public void onTrimMemory(Application app, int level) {
    }

    private void setupVirtualApkPluginManager(Context context){
        try {
            FreeReflect.hookAccessingHiddenFiledPermission();
            VirtualApk.getInstance(context).init(new PluginManager.Configuration());
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    private void setupMsaGetDeviceInfo(Context context){
        try {
            AndroidOaidProvider.init(context);
            AndroidOaidProvider.requestOaid(context);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    private void setupDynamicPermissionSupport(Context context){
        try{
            PermissionBirdge.setup();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}
