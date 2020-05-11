package droid.game.plugin.manager;

import android.os.Build;

import java.lang.reflect.Method;

public class FreeReflect {
    public static Method forNameMethod;
    public static Method getMethodMethod;

    static Class vmRuntimeClass;
    static Method addWhiteListMethod;

    static Object vmRuntime;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                getMethodMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
                forNameMethod = Class.class.getDeclaredMethod("forName", String.class);
                vmRuntimeClass = (Class) forNameMethod.invoke(null, "dalvik.system.VMRuntime");
                addWhiteListMethod = (Method) getMethodMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
                Method getVMRuntimeMethod = (Method) getMethodMethod.invoke(vmRuntimeClass, "getRuntime", null);
                vmRuntime = getVMRuntimeMethod.invoke(null);
            } catch (Exception e) {
                //Log.e("ReflectionUtils", "error get methods", e);
                e.printStackTrace();
            }
        }
    }

    public static boolean hookAccessingHiddenFiledPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                addReflectionWhiteList("L");
                return true;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static void addReflectionWhiteList(String... memberSigs) throws Throwable {
        addWhiteListMethod.invoke(vmRuntime, new Object[] {memberSigs});
    }
}
