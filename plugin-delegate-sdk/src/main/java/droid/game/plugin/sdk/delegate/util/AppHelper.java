package droid.game.plugin.sdk.delegate.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import droid.game.common.global.Global;

public class AppHelper {
    public static String getAppName(){
        Context context = Global.getApplication();
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //获取应用 信息
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            //获取albelRes
            int labelRes = applicationInfo.labelRes;
            if (!TextUtils.isEmpty(applicationInfo.nonLocalizedLabel) && labelRes == 0){
                return applicationInfo.nonLocalizedLabel.toString();
            }
            //返回App的名称
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }
}
