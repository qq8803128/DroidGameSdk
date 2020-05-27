package droid.game.plugin.sdk.delegate.ui.online.page.login;

import android.view.View;
import android.widget.EditText;
import droid.game.butterknife.annotation.BindView;
import droid.game.butterknife.annotation.OnClick;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.plugin.sdk.delegate.ui.online.dialog.LoginDialog;
import droid.game.plugin.sdk.delegate.ui.online.page.Page;

public class LoginPhone extends Page<LoginDialog> {
    @BindView(R3.id.droid_game_sdk_self_edit_mobile_login_phone_number)
    EditText mEdtMobileLoginNumber;

    @BindView(R3.id.droid_game_sdk_self_edit_mobile_login_phone_code)
    EditText mEdtMobileLoginCode;

    @OnClick({R3.id.droid_game_sdk_self_open_name_login_from_mobile_login,})
    void onBtnBackToNameLoginPageFromMobileLoginClicked(){
        getDialog().showPage(LoginDialog.LOGIN_NAME);
    }

    public LoginPhone(View itemView, LoginDialog dialog) {
        super(itemView, dialog);
    }

}
