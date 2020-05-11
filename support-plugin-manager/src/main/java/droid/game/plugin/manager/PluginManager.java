package droid.game.plugin.manager;

import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.*;
import java.util.List;

public abstract class PluginManager {
    private static PluginManager sPluginManager;

    public static PluginManager manager() {
        return sPluginManager;
    }

    private Configuration mConfiguration;
    private Context mContext;

    public PluginManager(Context context) {
        super();
        sPluginManager = this;
        mContext = context;
    }

    public Configuration getConfiguration() {
        return mConfiguration;
    }

    protected void init(Configuration configuration) {
        mConfiguration = configuration;
    }

    public boolean getAssets(String name,File output){
        long duration = System.currentTimeMillis();
        if (output.isFile() && output.exists()){
            return true;
        }
        InputStream in = null;
        OutputStream out = null;
        boolean result = false;
        try{
            File tempFile = new File(output.getAbsolutePath() + ".tmp");
            if (tempFile.exists() && tempFile.isFile()){
                tempFile.delete();
            }
            in = mContext.getAssets().open(name);
            out = new FileOutputStream(tempFile.getAbsolutePath());
            byte[] bytes = new byte[1024];
            int i;
            while ((i = in.read(bytes)) != -1) {
                out.write(bytes, 0, i);
            }

            if (tempFile != null && tempFile.exists() && tempFile.isFile()){
                tempFile.renameTo(output);
            }
            Log.d("Assets","relase file time:" + (System.currentTimeMillis() - duration));
            result = true;
        }catch (Throwable e){

        }finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public abstract Context getHostContext();

    public abstract Application getHostApplication();

    public abstract void loadPlugin(File apk) throws Exception;

    public abstract LoadedApk getLoadedApk(Intent intent);

    public abstract LoadedApk getLoadedApk(ComponentName component);

    public abstract LoadedApk getLoadedApk(String packageName);

    public abstract List<LoadedApk> getAllLoadedApks();

    public abstract Instrumentation getInstrumentation();

    public abstract void addCallback(Callback callback);

    public abstract void removeCallback(Callback callback);

    public void loadInternalPlugins() {
        long duration0 = System.currentTimeMillis();
        try {
            Context ctx = getHostContext();
            AssetManager assetManager = getHostContext().getAssets();
            String[] plugins = assetManager.list(getConfiguration().getInternalPluginsDir());
            for (String plugin : plugins){
                if (plugin.endsWith(".apk")){
                    long duration1 = System.currentTimeMillis();
                    File file = new File(
                            ctx.getDir(getConfiguration().getApkDir(),Context.MODE_PRIVATE),
                            plugin
                    );
                    if (load(assetManager,file,getConfiguration().getInternalPluginsDir(),plugin)){
                        Log.e("releaseInternalPlugin","name:" + plugin + "---duration:" + (System.currentTimeMillis() - duration1));
                        try {
                            loadPlugin(file);
                        }catch (Throwable e){
                            e.printStackTrace();
                        }
                    }
                    Log.e("loadInternalPlugin","name:" + plugin + "---duration:" + (System.currentTimeMillis() - duration1));
                }
            }
            Log.e("loadInternalPlugins","duation:"  + (System.currentTimeMillis() - duration0));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private boolean load(AssetManager am,File file,String path,String name){
        if (file.exists() && file.isFile()){
            return true;
        }

        File tempFile = new File(file.getAbsolutePath() + ".temp");
        if (tempFile.exists() && tempFile.isFile()){
            tempFile.delete();
        }

        boolean result = false;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = am.open(path + "/" + name);
            out = new FileOutputStream(tempFile.getAbsolutePath());
            byte[] bytes = new byte[1024];
            int i;
            while ((i = in.read(bytes)) != -1) {
                out.write(bytes, 0, i);
            }
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            file = null;
            result = false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (tempFile != null && tempFile.exists() && tempFile.isFile()){
            tempFile.renameTo(file);
        }
        return result;
    }

    public static class Configuration {
        private String optimizeDir = "va_dex";
        private String nativeDir = "va_lib";
        private String apkDir = "va_apk";
        private boolean combineResources = false;
        private boolean combineClassLoader = true;
        private int maxSingleTopCount = 0;
        private int maxSingleTaskCount = 0;
        private int maxSingleInstanceCount = 0;
        private String corePackage = "droid.game.virtualapk.core";
        private String stubStandardActivityFormat = "%s.A$%d";
        private String stubSingleTopActivityFormat = "%s.B$%d";
        private String stubSingleTaskActivityFormat = "%s.C$%d";
        private String stubSingleInstanceActivityFormat = "%s.D$%d";
        private String fileName = "vacfg";
        private boolean hookSystem = true;
        private boolean debug = true;
        private String internalPluginsDir = "android.gamecenter.system/internal_plugins";

        public String getApkDir() {
            return apkDir;
        }

        public void setApkDir(String apkDir) {
            this.apkDir = apkDir;
        }

        public String getInternalPluginsDir() {
            return internalPluginsDir;
        }

        public void setInternalPluginsDir(String internalPluginsDir) {
            this.internalPluginsDir = internalPluginsDir;
        }

        public Configuration setOptimizeDir(String dir) {
            this.optimizeDir = dir;
            return this;
        }

        public Configuration setNativeDir(String dir) {
            this.nativeDir = dir;
            return this;
        }

        public Configuration setCombineResources(boolean combineResources) {
            this.combineResources = combineResources;
            return this;
        }

        public Configuration setCombineClassLoader(boolean combineClassLoader) {
            this.combineClassLoader = combineClassLoader;
            return this;
        }

        public Configuration setMaxSingleTopCount(int maxSingleTopCount) {
            this.maxSingleTopCount = maxSingleTopCount;
            return this;
        }

        public Configuration setMaxSingleTaskCount(int maxSingleTaskCount) {
            this.maxSingleTaskCount = maxSingleTaskCount;
            return this;
        }

        public Configuration setMaxSingleInstanceCount(int maxSingleInstanceCount) {
            this.maxSingleInstanceCount = maxSingleInstanceCount;
            return this;
        }

        public Configuration setCorePackage(String corePackage) {
            this.corePackage = corePackage;
            return this;
        }

        public Configuration setStubStandardActivityFormat(String stubStandardActivityFormat) {
            this.stubStandardActivityFormat = stubStandardActivityFormat;
            return this;
        }

        public Configuration setStubSingleTopActivityFormat(String stubSingleTopActivityFormat) {
            this.stubSingleTopActivityFormat = stubSingleTopActivityFormat;
            return this;
        }

        public Configuration setStubSingleTaskActivityFormat(String stubSingleTaskActivityFormat) {
            this.stubSingleTaskActivityFormat = stubSingleTaskActivityFormat;
            return this;
        }

        public Configuration setStubSingleInstanceActivityFormat(String stubSingleInstanceActivityFormat) {
            this.stubSingleInstanceActivityFormat = stubSingleInstanceActivityFormat;
            return this;
        }

        public Configuration setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Configuration setHookSystem(boolean hookSystem){
            this.hookSystem = hookSystem;
            return this;
        }

        public Configuration setDebug(boolean debug){
            this.debug = debug;
            return this;
        }

        public String getOptimizeDir() {
            return optimizeDir;
        }

        public String getNativeDir() {
            return nativeDir;
        }

        public boolean isCombineResources() {
            return combineResources;
        }

        public boolean isCombineClassLoader() {
            return combineClassLoader;
        }

        public int getMaxSingleTopCount() {
            return maxSingleTopCount;
        }

        public int getMaxSingleTaskCount() {
            return maxSingleTaskCount;
        }

        public int getMaxSingleInstanceCount() {
            return maxSingleInstanceCount;
        }

        public String getCorePackage() {
            return corePackage;
        }

        public String getStubStandardActivityFormat() {
            return stubStandardActivityFormat;
        }

        public String getStubSingleTopActivityFormat() {
            return stubSingleTopActivityFormat;
        }

        public String getStubSingleTaskActivityFormat() {
            return stubSingleTaskActivityFormat;
        }

        public String getStubSingleInstanceActivityFormat() {
            return stubSingleInstanceActivityFormat;
        }

        public String getFileName() {
            return fileName;
        }

        public boolean isHookSystem(){
            return hookSystem;
        }

        public boolean isDebug(){
            return debug;
        }

        @Override
        public String toString() {
            try {
                String format = String.format(
                        "dex目录:%s\r\njni目录:%s\n合并资源:%s\n合并ClassLoader:%s\ncorePackage:%s\r\n" +
                                "singleTop{format:%s,count:%d}\r\n" +
                                "singleTask{format:%s,count:%d}\n" +
                                "singleInstance{format:%s,count:%d}\n" +
                                "standard{format:%s,count:1}\n" +
                                "sharedPreferencesName:%s\nhookSystem:%s\ndebug:%s",
                        optimizeDir, nativeDir, combineResources + "", combineClassLoader + "", corePackage,
                        stubSingleTopActivityFormat, maxSingleTopCount,
                        stubSingleTaskActivityFormat, maxSingleTaskCount,
                        stubSingleInstanceActivityFormat, maxSingleInstanceCount,
                        stubStandardActivityFormat,
                        fileName, hookSystem, debug
                );
                return format;
            }catch (Throwable e){
                e.printStackTrace();
            }
            return super.toString();
        }
    }

    public interface Callback {
        void onAddedLoadedPlugin(LoadedApk plugin);
    }
}
