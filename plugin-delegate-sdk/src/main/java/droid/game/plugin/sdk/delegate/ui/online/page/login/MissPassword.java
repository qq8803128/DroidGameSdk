package droid.game.plugin.sdk.delegate.ui.online.page.login;

import android.view.View;
import droid.game.butterknife.annotation.OnClick;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.plugin.sdk.delegate.ui.online.dialog.LoginDialog;
import droid.game.plugin.sdk.delegate.ui.online.page.Page;

public class MissPassword extends Page<LoginDialog> {
    @OnClick({R3.id.droid_game_sdk_self_open_name_login_from_miss_password})
    void onBtnBackToNameLoginPageFromMobileLoginClicked(){
        getDialog().showPage(LoginDialog.LOGIN_NAME);
    }

    public MissPassword(View itemView, LoginDialog dialog) {
        super(itemView, dialog);
    }
}
