package droid.game.plugin.sdk.delegate.sdk;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import droid.game.common.keep.Consumer;
import droid.game.core.keep.IChannel;
import droid.game.core.parameter.Parameter;
import droid.game.core.result.Result;
import droid.game.plugin.sdk.delegate.ui.online.LoginController;

/**
 * 网游sdk
 */
public class OnLineSdk implements IChannel {
    @Override
    public void init(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {
        success.accept(new Result.Init(true,parameter,parameter,""));
    }

    @Override
    public void login(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {
        new LoginController().login(activity,parameter,success,failed);
    }

    @Override
    public void logout(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {

    }

    @Override
    public void role(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {

    }

    @Override
    public void pay(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {

    }

    @Override
    public void exit(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {

    }

    @Override
    public void exec(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {

    }

    @Override
    public void onCreate(Activity activity) {

    }

    @Override
    public void onResume(Activity activity) {

    }

    @Override
    public void onRestart(Activity activity) {

    }

    @Override
    public void onStart(Activity activity) {

    }

    @Override
    public void onPause(Activity activity) {

    }

    @Override
    public void onStop(Activity activity) {

    }

    @Override
    public void onDestroy(Activity activity) {

    }

    @Override
    public void onNewIntent(Activity activity, Intent intent) {

    }

    @Override
    public void onConfigurationChanged(Activity activity, Configuration newConfig) {

    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {

    }
}
