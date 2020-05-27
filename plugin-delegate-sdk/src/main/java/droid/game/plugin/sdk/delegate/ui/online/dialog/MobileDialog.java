package droid.game.plugin.sdk.delegate.ui.online.dialog;

import android.content.Context;
import droid.game.butterknife.annotation.OnClick;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.x2c.annotation.Xml;

@Xml(layouts = {"droid_game_sdk_self_dialog_mobile"})
public class MobileDialog extends Dialog<MobileDialog> {
    @OnClick(R3.id.droid_game_sdk_self_cancel_bind_mobile)
    void onBtnCancelBindMobileClicked(){
        getDialog().dismiss();
    }

    public MobileDialog(Context context) {
        super(context);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected String getLayout() {
        return R3.layout.droid_game_sdk_self_dialog_mobile;
    }

}
