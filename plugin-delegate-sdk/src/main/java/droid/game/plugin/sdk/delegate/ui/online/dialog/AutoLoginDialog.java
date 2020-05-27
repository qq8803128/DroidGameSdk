package droid.game.plugin.sdk.delegate.ui.online.dialog;

import android.content.Context;
import android.view.Gravity;
import droid.game.butterknife.annotation.OnClick;
import droid.game.common.dialog.UIDialog;
import droid.game.common.dialog.widget.UIDialogRootLayout;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.x2c.annotation.Xml;

@Xml(layouts = {"droid_game_sdk_self_dialog_login_auto"})
public class AutoLoginDialog extends Dialog<AutoLoginDialog> {
    @OnClick(R3.id.droid_game_sdk_self_auto_login_switch)
    void onBtnSwitchAccountClicked(){
        getDialog().cancel();
    }

    public AutoLoginDialog(Context context) {
        super(context,1.0f);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setGravity(Gravity.BOTTOM);
        setDimAmount(0f);
    }

    @Override
    protected String getLayout() {
        return R3.layout.droid_game_sdk_self_dialog_login_auto;
    }

    @Override
    public void onAfterCreate(UIDialog dialog, UIDialogRootLayout rootLayout, Context context) {
        super.onAfterCreate(dialog, rootLayout, context);
    }
}
