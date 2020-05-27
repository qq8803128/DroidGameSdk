package droid.game.plugin.sdk.delegate.ui.online.dialog;

import android.content.Context;

import static droid.game.common.util.Utils.windowWidth;

public class UserDialog extends Dialog<UserDialog>{
    public UserDialog(Context context) {
        super(context,1.0f);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setLayout(windowWidth(),windowWidth(),0,0);
    }

    @Override
    protected String getLayout() {
        return null;
    }
}
