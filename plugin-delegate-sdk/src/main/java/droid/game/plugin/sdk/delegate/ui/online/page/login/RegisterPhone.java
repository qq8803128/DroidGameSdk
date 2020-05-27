package droid.game.plugin.sdk.delegate.ui.online.page.login;

import android.view.View;
import droid.game.butterknife.annotation.BindView;
import droid.game.butterknife.annotation.OnClick;
import droid.game.common.span.Span;
import droid.game.common.widget.UISpanTouchFixTextView;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.plugin.sdk.delegate.ui.online.dialog.LoginDialog;
import droid.game.plugin.sdk.delegate.ui.online.page.Page;

public class RegisterPhone extends Page<LoginDialog> {
    @BindView(R3.id.droid_game_sdk_self_open_name_register_from_mobile_register)
    UISpanTouchFixTextView mOpenNameLogin;

    @BindView(R3.id.droid_game_sdk_self_register_mobile_user_protocol)
    UISpanTouchFixTextView mMobileUserProtocol;

    @OnClick({R3.id.droid_game_sdk_self_open_name_login_from_mobile_register})
    void onBtnBackToNameLoginPageFromMobileLoginClicked(){
        getDialog().showPage(LoginDialog.LOGIN_NAME);
    }


    @OnClick(R3.id.droid_game_sdk_self_open_name_register_from_mobile_register)
    void onBtnOpenNameLoginPageClicked(){
        getDialog().setQuickRegister(false);
        getDialog().showPage(LoginDialog.REGISTER_NAME);
    }

    public RegisterPhone(View itemView, LoginDialog dialog) {
        super(itemView, dialog);
    }

    @Override
    protected void initialize() {
        super.initialize();
        configSpanText();
    }

    private void configSpanText(){
        Span span = new Span("(不推荐)用户名注册").add();
        span.lightText("(不推荐)").textColor(0xffa0a0a0,0xffa0a0a0);
        mOpenNameLogin.setMovementMethodDefault();
        mOpenNameLogin.setText(span.build());

        span = new Span("注册即同意《用户协议》").add();
        span.lightText("《用户协议》").textColor(0xffff8200,0xffff8200).listener(new Span.OnClickSpanListener() {
            @Override
            public void onClick(View v, String lightText) {

            }
        });
        CharSequence charSequence = span.build();
        mMobileUserProtocol.setMovementMethodDefault();
        mMobileUserProtocol.setText(charSequence);
    }
}
