package droid.game.plugin.sdk.delegate.ui.online.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import droid.game.butterknife.annotation.BindView;
import droid.game.butterknife.annotation.OnClick;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.plugin.sdk.delegate.ui.online.BaseUi;
import droid.game.x2c.annotation.Xml;

@Xml(layouts = {"droid_game_sdk_self_dialog_certification"})
public class CertificationDialog extends BaseUi<CertificationDialog> {
    @BindView(R3.id.droid_game_sdk_self_certification_page)
    ViewGroup mPager;

    @OnClick(R3.id.droid_game_sdk_self_skip_certification)
    void onBtnSkipCertificationClicked(){
        mPager.getChildAt(0).setVisibility(View.GONE);
        mPager.getChildAt(1).setVisibility(View.VISIBLE);
    }

    @OnClick(R3.id.droid_game_sdk_self_open_certification_page)
    void onBtnOpenCertificationClicked(){
        mPager.getChildAt(1).setVisibility(View.GONE);
        mPager.getChildAt(0).setVisibility(View.VISIBLE);
    }

    @OnClick(R3.id.droid_game_sdk_self_cancel_certification)
    void onBtnCancelCerttificationClicked(){
        getDialog().dismiss();
    }

    public CertificationDialog(Context context) {
        super(context);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    protected String getLayout() {
        return R3.layout.droid_game_sdk_self_dialog_certification;
    }
}
