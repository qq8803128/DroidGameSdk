package droid.game.core.bridge.splash;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import droid.game.core.bridge.bridge.Attribute;
import droid.game.core.keep.ISplash;

public class Splash implements ISplash {
    public static Splash splash(){
        return Holder.holder;
    }

    private boolean mShow = false;
    private Runnable mRunnable;
    public Splash() {
        super();
    }

    @Override
    public void show(Activity activity, Runnable runnable) {
        if (mShow){
            runnable.run();
            return;
        }

        mRunnable = runnable;
        switch (Attribute.system().getSplashStyle()) {
            case "Dialog":
            case "dialog":
                startSplashDialog(activity);
                break;
            case "Activity":
            case "activity":
                startSplashActivity(activity);
                break;
            default:
                startSplashNull(activity);
                break;
        }
    }

    private void startSplashDialog(Activity activity){
        new SplashDialog(activity)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create()
                .addOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        onCompleted();
                    }
                })
                .show();
    }

    private void startSplashActivity(Activity activity){
        activity.startActivity(new Intent(activity,SplashActivity.class));
    }

    private void startSplashNull(Activity activity){
        onCompleted();
    }

    public void onCompleted(){
        mShow = true;
        mRunnable.run();
    }

    private static class Holder{
        private static final Splash holder = new Splash();
    }
}
