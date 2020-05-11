package droid.game.virtualapk.internal.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.Method;

public final class WebViewResourceHelper {
    public static boolean addChromeResourceIfNeeded(Context context) {
        String resourceDir = getWebViewResourceDir(context);
        if (TextUtils.isEmpty(resourceDir)) {
            return false;
        }

        try {
            Method m = getAddAssetPathMethod();
            if (m != null) {
                int ret = (int) m.invoke(context.getAssets(), resourceDir);
                return ret > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Method getAddAssetPathMethod() {
        Method m = null;
        Class c = AssetManager.class;
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                m = c.getDeclaredMethod("addAssetPathAsSharedLibrary", String.class);
                m.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // Do Nothing
                e.printStackTrace();
            }
            return m;
        }

        try {
            m = c.getDeclaredMethod("addAssetPath", String.class);
            m.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return m;
    }

    private static String getWebViewResourceDir(Context context) {
        String pkgName = getWebViewPackageName();
        if (TextUtils.isEmpty(pkgName)) {
            return null;
        }

        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(getWebViewPackageName(), PackageManager.GET_SHARED_LIBRARY_FILES);
            return pi.applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getWebViewPackageName() {
        int sdk = Build.VERSION.SDK_INT;

        if (sdk <= 20) {
            return null;
        }

        switch (sdk) {
            case 28:
                return getWEbViewPackageName4P();
            case 21:
            case 22:
                return getWebViewPackageName4Lollipop();
            case 23:
                return getWebViewPackageName4M();
            case 24:
                return getWebViewPackageName4N();
            case 25:
            default:
                return getWebViewPackageName4More();
        }
    }

    private static String getWebViewPackageName4Lollipop() {
        try {
            return (String) invokeStaticMethod("android.webkit.WebViewFactory", "getWebViewPackageName", null);
        } catch (Throwable e) {
            //e.printStackTrace();
        }

        return "com.google.android.webview";
    }

    private static String getWebViewPackageName4M() {
        return getWebViewPackageName4Lollipop();
    }

    private static String getWEbViewPackageName4P(){
        try {
            Context c = (Context) invokeStaticMethod("android.webkit.WebViewFactory", "getWebViewContextAndSetProvider", null);
            return c.getApplicationInfo().packageName;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "com.google.android.webview";
    }

    private static String getWebViewPackageName4N() {
        try {
            Context c = (Context) invokeStaticMethod("android.webkit.WebViewFactory", "getWebViewContextAndSetProvider", null);
            return c.getApplicationInfo().packageName;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "com.google.android.webview";
    }

    private static String getWebViewPackageName4More() {
        return getWebViewPackageName4N();
    }

    static Object invokeStaticMethod(String clzName, String methodName, Class<?>[] methodParamTypes, Object... methodParamValues) {
        try {
            Class clz = Class.forName(clzName);
            if (clz != null) {
                Method method = clz.getDeclaredMethod(methodName, methodParamTypes);
                if (method != null) {
                    method.setAccessible(true);
                    return method.invoke(null, methodParamValues);
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }
}
