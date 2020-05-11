package cn.droid.game.demo;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import com.didichuxing.doraemonkit.DoraemonKit;
import droid.game.core.bridge.DroidSdk;

import java.io.File;

public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        DroidSdk.Application.attachBaseContext(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DoraemonKit.install(this,null,"8055142852a4f4f3b4ee1401b6bd9616");

        cleanCache();
        //InjectOkHttpClient.inject();
        DroidSdk.Application.onCreate(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DroidSdk.Application.onTerminate(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DroidSdk.Application.onConfigurationChanged(this,newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        DroidSdk.Application.onLowMemory(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        DroidSdk.Application.onTrimMemory(this,level);
    }

    private void cleanCache(){
        deleteDirWihtFile(getCacheDir());
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                Log.e("TAG",file.getAbsolutePath() + "");
                file.delete(); // 删除所有文件
            }
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }
}
