package droid.game.xsdk.inner;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import droid.game.android.oaid.AndroidOaidProvider;
import droid.game.common.keep.Consumer;
import droid.game.core.keep.IChannel;
import droid.game.core.parameter.Parameter;
import droid.game.core.result.Result;

public class InnerSdk implements IChannel {
    private IChannel mDelegateChannel;

    @Override
    public void init(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {
        getDelegateChannel().init(activity,parameter,success,failed);
    }

    @Override
    public void login(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {
        getDelegateChannel().login(activity,parameter,success,failed);
    }

    @Override
    public void logout(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {
        getDelegateChannel().logout(activity,parameter,success,failed);
    }

    @Override
    public void role(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {
        getDelegateChannel().role(activity,parameter,success,failed);
    }

    @Override
    public void pay(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {
        getDelegateChannel().pay(activity,parameter,success,failed);
    }

    @Override
    public void exit(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {
        getDelegateChannel().exit(activity,parameter,success,failed);
    }

    @Override
    public void exec(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {
        getDelegateChannel().exec(activity,parameter,success,failed);
    }

    @Override
    public void onCreate(Activity activity) {
        getDelegateChannel().onCreate(activity);
    }

    @Override
    public void onResume(Activity activity) {
        getDelegateChannel().onResume(activity);
    }

    @Override
    public void onRestart(Activity activity) {
        getDelegateChannel().onRestart(activity);
    }

    @Override
    public void onStart(Activity activity) {
        getDelegateChannel().onStart(activity);
    }

    @Override
    public void onPause(Activity activity) {
        getDelegateChannel().onPause(activity);
    }

    @Override
    public void onStop(Activity activity) {
        getDelegateChannel().onStop(activity);
    }

    @Override
    public void onDestroy(Activity activity) {
        getDelegateChannel().onDestroy(activity);
    }

    @Override
    public void onNewIntent(Activity activity, Intent intent) {
        getDelegateChannel().onNewIntent(activity,intent);
    }

    @Override
    public void onConfigurationChanged(Activity activity, Configuration newConfig) {
        getDelegateChannel().onConfigurationChanged(activity,newConfig);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        getDelegateChannel().onActivityResult(activity,requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        getDelegateChannel().onRequestPermissionsResult(activity,requestCode,permissions,grantResults);
    }

    private IChannel getDelegateChannel(){
        if (mDelegateChannel == null){
            mDelegateChannel = DelegateSdk.create();
        }
        return mDelegateChannel;
    }
}
