package droid.game.plugin.sdk.delegate.ui.online.page.login;

import android.view.View;
import android.widget.EditText;
import droid.game.butterknife.annotation.BindString;
import droid.game.butterknife.annotation.BindView;
import droid.game.butterknife.annotation.OnClick;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.plugin.sdk.delegate.ui.online.dialog.LoginDialog;
import droid.game.plugin.sdk.delegate.ui.online.page.Page;
import droid.game.plugin.sdk.delegate.util.InputHelper;

public class LoginName extends Page<LoginDialog> {
    @BindString(R3.string.droid_game_sdk_self_english_digits)
    String mEnglishDigits;

    @BindString(R3.string.droid_game_sdk_self_password_digits)
    String mPasswordDigits;

    @BindView(R3.id.droid_game_sdk_self_edit_name_login_account)
    EditText mEdtNameLoginAccount;

    @BindView(R3.id.droid_game_sdk_self_edit_name_login_password)
    EditText mEdtNameLoginPassword;

    @OnClick(R3.id.droid_game_sdk_self_open_mobile_login_page)
    void onBtnOpenMobileLoginPageClicked(){
        getDialog().showPage(LoginDialog.LOGIN_MOBILE);
    }

    @OnClick(R3.id.droid_game_sdk_self_open_quick_login_page)
    void onBtnOpenQuickLoginPageClicked(){
        getDialog().setQuickRegister(true);
        getDialog().showPage(LoginDialog.REGISTER_NAME);
    }

    @OnClick(R3.id.droid_game_sdk_self_open_miss_password)
    void onBtnOpenMissPasswordPageClicked(){
        getDialog().showPage(LoginDialog.MISS_PASSWORD);
    }

    @OnClick({R3.id.droid_game_sdk_self_open_mobile_register_page,})
    void onBtnOpenMobileRegisterPageClicked(){
        getDialog().showPage(LoginDialog.REGISTER_MOBILE);
    }

    @OnClick(R3.id.droid_game_sdk_self_do_name_login)
    void onBtnDoNameLoginClicked(){
        getDialog().getDialog().dismiss();
    }

    public LoginName(View itemView, LoginDialog dialog) {
        super(itemView, dialog);
    }

    @Override
    protected void initialize() {
        super.initialize();
        configEditText();
    }

    private void configEditText(){
        InputHelper.with(mEdtNameLoginAccount)
                .addFilter(new InputHelper.LengthFilter(20))
                .addFilter(new InputHelper.DigitsFilter(mEnglishDigits))
                .setSingleLine()
                .setInputText();

        InputHelper.with(mEdtNameLoginPassword)
                .addFilter(new InputHelper.LengthFilter(20))
                .addFilter(new InputHelper.DigitsFilter(mPasswordDigits))
                .setSingleLine()
                .setInputText();
    }
}
