package droid.game.plugin.sdk.delegate.ui.online;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;
import droid.game.android.floater.window.FtImageView;
import droid.game.android.floater.window.FtWindowManager;
import droid.game.butterknife.ButterKnife;
import droid.game.butterknife.Unbinder;
import droid.game.common.keep.Action;
import droid.game.common.keep.Consumer;
import droid.game.common.util.UIResourcesHelper;
import droid.game.core.parameter.Parameter;
import droid.game.core.result.Result;
import droid.game.plugin.sdk.delegate.Constants;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.plugin.sdk.delegate.ui.online.dialog.*;

public class LoginController {
    private FtWindowManager mFtWindowManager;
    private FtImageView mFtImageView;

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

    public void logout(Activity activity, Parameter parameter, Consumer<Result> success, Consumer<Result> failed) {
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
                        showFloater(activity);
                    }
                })
                .show();
    }

    private void showLoading(Activity activity) {

    }

    private void hideLoading() {

    }

    public void showFloater(final Activity activity){
        if (mFtWindowManager == null){
            mFtWindowManager = new FtWindowManager.Builder()
                    .addMenu(
                            new FtWindowManager.MenuItem()
                            .setDrawable(getDrawable(R3.mipmap.droid_game_sdk_self_ic_floater_window_manager_home))
                            .setText("主页")
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new UserDialog(activity)
                                            .show();
                                }
                            })
                    )
                    .addMenu(
                            new FtWindowManager.MenuItem()
                            .setDrawable(getDrawable(R3.mipmap.droid_game_sdk_self_ic_floater_window_manager_gift))
                            .setText("礼包")
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new UserDialog(activity)
                                            .show();
                                }
                            })
                    )
                    .addMenu(
                            new FtWindowManager.MenuItem()
                            .setDrawable(getDrawable(R3.mipmap.droid_game_sdk_self_ic_floater_window_manager_client))
                            .setText("客服")
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                    )
                    .setSize(48)
                    .create(activity);
            mFtImageView = new FtImageView(activity);
        }

        mFtWindowManager.addView(mFtImageView);
    }

    public void hideFloater(Activity activity){
        try {
            mFtWindowManager.removeView(mFtImageView);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    public Drawable getDrawable(String id){
        return UIResourcesHelper.getDrawable(getPluginContext(),id);
    }

    public Context getPluginContext() {
        return Constants.getSelfContext();
    }

}
