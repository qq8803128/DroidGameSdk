package droid.game.xsdk.inner;

import droid.game.core.keep.IChannel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

final class DelegateSdk implements InvocationHandler {
    static IChannel create(){
        return (IChannel) Proxy.newProxyInstance(DelegateSdk.class.getClassLoader(),new Class[]{IChannel.class},new DelegateSdk());
    }

    private static final String CLASS_NAME = "droid.game.plugin.sdk.delegate.DelegateSdk";
    private Object mTarget;

    public DelegateSdk() {
        super();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        checkTarget();
        if (mTarget != null){
            method.invoke(mTarget,args);
        }
        return null;
    }

    private void checkTarget(){
        try{
            if (mTarget == null) mTarget = Class.forName(CLASS_NAME).newInstance();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}
