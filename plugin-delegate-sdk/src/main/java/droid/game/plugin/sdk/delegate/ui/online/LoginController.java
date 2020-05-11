package droid.game.plugin.sdk.delegate.ui.online;


import android.app.Activity;
import android.content.DialogInterface;
import droid.game.common.keep.Action;
import droid.game.common.keep.Consumer;
import droid.game.core.parameter.Parameter;
import droid.game.core.result.Result;
import droid.game.plugin.sdk.delegate.ui.online.dialog.CertificationDialog;
import droid.game.plugin.sdk.delegate.ui.online.dialog.LoginDialog;
import droid.game.plugin.sdk.delegate.ui.online.dialog.MobileDialog;
import droid.game.plugin.sdk.delegate.ui.online.dialog.PaymentDialog;

public class LoginController {
    public void login(final Activity activity, final Parameter parameter, final Consumer<Result> success, final Consumer<Result> failed) {
        showLoading(activity);
        requestQuickLogin(new Action(){
            @Override
            public void run() {
                hideLoading();
                doLogin(activity,parameter,success,failed);
            }
        });
    }

    private boolean isFirstLogin() {
        return false;
    }

    private void requestQuickLogin(Action action) {
        action.run();
    }

    private void doLogin(final Activity activity, final Parameter parameter, final Consumer<Result> success, final Consumer<Result> failed){
        new LoginDialog(activity)
                .create()
                .addOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        checkCertification(activity,parameter,success,failed);
                    }
                })
                .show();
    }

    private void checkCertification(final Activity activity, final Parameter parameter, final Consumer<Result> success, final Consumer<Result> failed){
        boolean checkCertification = true;
        final Action checkMobileAction = new Action() {
            @Override
            public void run() {
                checkMobile(activity,parameter,success,failed);
            }
        };
        if (checkCertification) {
            new CertificationDialog(activity)
                    .create()
                    .addOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            checkMobileAction.run();
                        }
                    })
                    .show();
        }else{
            checkMobileAction.run();
        }
    }

    private void checkMobile(final Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed){
        new MobileDialog(activity)
                .create()
                .addOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        new PaymentDialog(activity).show();
                    }
                })
                .show();
    }

    private void showLoading(Activity activity) {

    }

    private void hideLoading() {

    }
}
