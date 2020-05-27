package droid.game.plugin.sdk.delegate.ui.online.page.login;

import android.view.View;
import droid.game.butterknife.annotation.BindView;
import droid.game.butterknife.annotation.OnClick;
import droid.game.common.span.Span;
import droid.game.common.widget.UISpanTouchFixTextView;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.plugin.sdk.delegate.ui.online.dialog.LoginDialog;
import droid.game.plugin.sdk.delegate.ui.online.page.Page;

public class RegisterName extends Page<LoginDialog> {
    @BindView(R3.id.droid_game_sdk_self_register_name_user_protocol)
    UISpanTouchFixTextView mNameUserProtocol;

    @OnClick({R3.id.droid_game_sdk_self_open_mobile_register_from_name_register})
    void onBtnOpenMobileRegisterPageClicked(){
        getDialog().showPage(LoginDialog.REGISTER_MOBILE);
    }

    public RegisterName(View itemView, LoginDialog dialog) {
        super(itemView, dialog);
    }

    @Override
    protected void initialize() {
        super.initialize();
        configSpanText();
    }

    private void configSpanText(){
        Span span = new Span("注册即同意《用户协议》").add();
        span.lightText("《用户协议》").textColor(0xffff8200,0xffff8200).listener(new Span.OnClickSpanListener() {
            @Override
            public void onClick(View v, String lightText) {
            }
        });
        CharSequence charSequence = span.build();
        mNameUserProtocol.setMovementMethodDefault();
        mNameUserProtocol.setText(charSequence);
    }
}
