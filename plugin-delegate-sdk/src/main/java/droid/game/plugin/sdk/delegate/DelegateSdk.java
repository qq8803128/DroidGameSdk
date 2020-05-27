package droid.game.plugin.sdk.delegate;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import droid.game.common.keep.Consumer;
import droid.game.core.bridge.bridge.Attribute;
import droid.game.core.keep.IChannel;
import droid.game.core.parameter.Parameter;
import droid.game.core.result.Result;
import droid.game.plugin.sdk.delegate.db.LiteOrmImpl;
import droid.game.plugin.sdk.delegate.sdk.OffLineSdk;
import droid.game.plugin.sdk.delegate.sdk.OnLineSdk;

public class DelegateSdk implements IChannel {
    //网游和单机SDK分开处理
    private IChannel mChannel;

    @Override
    public void init(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {
        LiteOrmImpl.getInstance().init(activity);
        getSdk().init(activity,parameter,success,failed);
    }

    @Override
    public void login(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {
        getSdk().login(activity,parameter,success,failed);
    }

    @Override
    public void logout(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {
        getSdk().logout(activity,parameter,success,failed);
    }

    @Override
    public void role(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {
        getSdk().role(activity,parameter,success,failed);
    }

    @Override
    public void pay(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {
        getSdk().pay(activity,parameter,success,failed);
    }

    @Override
    public void exit(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {
        getSdk().exit(activity,parameter,success,failed);
    }

    @Override
    public void exec(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {
        getSdk().exec(activity,parameter,success,failed);
    }

    @Override
    public void onCreate(Activity activity) {
        getSdk().onCreate(activity);
    }

    @Override
    public void onResume(Activity activity) {
        getSdk().onResume(activity);
    }

    @Override
    public void onRestart(Activity activity) {
        getSdk().onRestart(activity);
    }

    @Override
    public void onStart(Activity activity) {
        getSdk().onStart(activity);
    }

    @Override
    public void onPause(Activity activity) {
        getSdk().onPause(activity);
    }

    @Override
    public void onStop(Activity activity) {
        getSdk().onStop(activity);
    }

    @Override
    public void onDestroy(Activity activity) {
        getSdk().onDestroy(activity);
    }

    @Override
    public void onNewIntent(Activity activity, Intent intent) {
        getSdk().onNewIntent(activity,intent);
    }

    @Override
    public void onConfigurationChanged(Activity activity, Configuration newConfig) {
        getSdk().onConfigurationChanged(activity,newConfig);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        getSdk().onActivityResult(activity,requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        getSdk().onRequestPermissionsResult(activity,requestCode,permissions,grantResults);
    }

    private IChannel getSdk(){
        if (mChannel == null){
            if (Attribute.system().isOnlineGame()){
                mChannel = new OnLineSdk();
            }else{
                mChannel = new OffLineSdk();
            }
        }
        return mChannel;
    }
}
