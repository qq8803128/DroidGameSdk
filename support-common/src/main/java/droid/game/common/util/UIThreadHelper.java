package droid.game.common.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

public class UIThreadHelper {
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 主线程执行
     * @param runnable
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static final void runNow(Runnable runnable) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    /**
     * 主线程延时执行
     * @param runnable
     * @param duration
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static final void runDelay(Runnable runnable,long duration){
        mHandler.postDelayed(runnable,duration);
    }
}
