package droid.game.core.bridge.bridge;

import android.text.TextUtils;
import droid.game.common.log.LogRecord;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

final class BrigeDelegate implements InvocationHandler {
    @Deprecated
    public static <T> T create( Class[] interfaces){
        return (T) Proxy.newProxyInstance(BrigeDelegate.class.getClassLoader(),interfaces,new BrigeDelegate());
    }

    public static <T> T create(String className, Class[] interfaces){
        return (T) Proxy.newProxyInstance(BrigeDelegate.class.getClassLoader(),interfaces,new BrigeDelegate(className));
    }

    private Object mTarget;

    public BrigeDelegate() {
        this(null);
    }

    public BrigeDelegate(String className) {
        if (!TextUtils.isEmpty(className)) {
            try {
                mTarget = Class.forName(className).newInstance();
            } catch (Throwable e) {

            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try{
            LogRecord.d(method);
        }catch (Throwable e){
            e.printStackTrace();
        }

        try{
            if (mTarget != null) {
                method.invoke(mTarget, args);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
        return null;
    }
}
