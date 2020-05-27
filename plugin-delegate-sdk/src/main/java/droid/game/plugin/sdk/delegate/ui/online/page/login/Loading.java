package droid.game.plugin.sdk.delegate.ui.online.page.login;

import android.view.View;
import android.view.ViewGroup;
import droid.game.butterknife.annotation.BindView;
import droid.game.common.widget.LoadingView;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.plugin.sdk.delegate.ui.online.dialog.LoginDialog;
import droid.game.plugin.sdk.delegate.ui.online.page.Page;

public class Loading extends Page<LoginDialog> {
    @BindView(R3.id.droid_game_sdk_self_dialog_loading_page)
    ViewGroup mLoading;

    public Loading(View itemView, LoginDialog dialog) {
        super(itemView, dialog);
    }

    private void setLoadingColor(){
        ((LoadingView)((ViewGroup)mLoading.getChildAt(0)).getChildAt(0)).setLoadingColor(0xffafafaf);
    }
}
