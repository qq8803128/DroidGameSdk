package droid.game.core.bridge;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import droid.game.common.global.Global;
import droid.game.core.bridge.bridge.Attribute;

import java.lang.reflect.Method;

public class DroidApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Global.setApplication(this);
        if (Attribute.system().multidexInstall()){
            install();
        }
        DroidSdk.Application.attachBaseContext(this);
    }

    private void install(){
        try{
            Class Multidex = Class.forName("android.support.multidex.Multidex");
            Method install = Multidex.getDeclaredMethod("install",Context.class);
            install.setAccessible(true);
            install.invoke(this,this);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
}
