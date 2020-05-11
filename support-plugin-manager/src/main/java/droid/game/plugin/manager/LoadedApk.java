package droid.game.plugin.manager;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public interface LoadedApk {
    String getLocation();
    String getPackageName();
    PackageManager getPackageManager();
    AssetManager getAssets();
    Resources getResources();
    ClassLoader getClassLoader();
    Context getHostContext();
    Context getPluginContext();
    Application getApplication();
    String getPackageResourcePath();
    String getCodePath();
    ApplicationInfo getApplicationInfo();
    PackageInfo getPackageInfo();
    String getLabel();
    Drawable getIcon();
}
