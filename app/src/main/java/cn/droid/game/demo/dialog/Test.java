package cn.droid.game.demo.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import cn.droid.game.demo.R;
import droid.game.common.dialog.UIDialog;
import droid.game.common.dialog.widget.UIDialogView;

public class Test extends BaseUi{
    public Test(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView(UIDialog dialog, UIDialogView dialogView, Context context) {
        return LayoutInflater.from(context).inflate(R.layout.droid_game_sdk_self_dialog_login_first,null);
    }
}
