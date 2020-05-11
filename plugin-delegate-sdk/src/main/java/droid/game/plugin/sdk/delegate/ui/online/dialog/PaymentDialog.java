package droid.game.plugin.sdk.delegate.ui.online.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import droid.game.butterknife.annotation.BindView;
import droid.game.butterknife.annotation.OnClick;
import droid.game.common.dialog.UIDialog;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.plugin.sdk.delegate.ui.online.BaseUi;
import droid.game.x2c.annotation.Xml;

@Xml(layouts = {"droid_game_sdk_self_dialog_payment"})
public class PaymentDialog extends BaseUi<PaymentDialog> {

    @BindView(R3.id.droid_game_sdk_self_dialog_payment_page)
    ViewGroup mPager;

    @OnClick(R3.id.droid_game_sdk_self_dialog_payment_swicth_pay_way)
    void onBtnSwitchPayWayClicked(){
        showPage(1);
    }

    @OnClick(R3.id.droid_game_sdk_self_dialog_payment_back_to_pay_page)
    void onBtnBackToPayPageClicked(){
        showPage(0);
    }

    @OnClick(R3.id.droid_game_sdk_self_dialog_payment_cancel)
    void onBtnCancelPaymentClicked(){
        getDialog().cancel();
    }

    public PaymentDialog(Context context) {
        super(context);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected String getLayout() {
        return R3.layout.droid_game_sdk_self_dialog_payment;
    }

    private void showPage(int newPage){

        try {
            UIDialog.hideSoftInputDialog(getDialog());
        }catch (Throwable e){
            e.printStackTrace();
        }

        int oldPageFlag = -1;
        for (int i = 0; i < mPager.getChildCount(); i++){
            if (mPager.getChildAt(i).getVisibility() == View.VISIBLE){
                oldPageFlag = i;
                break;
            }
        }

        if (oldPageFlag == -1){
            mPager.getChildAt(newPage).setVisibility(View.VISIBLE);
            return;
        }

        if (oldPageFlag != newPage){
            final int oldPage = oldPageFlag;
            mPager.getChildAt(newPage).setVisibility(View.VISIBLE);

            float f1 = oldPageFlag < newPage ? 1f : -1f;
            float f2 = oldPageFlag < newPage ? -1f : 1f;
            TranslateAnimation animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF,f1,Animation.RELATIVE_TO_SELF,0f,
                    Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f
            );
            animation.setDuration(300);
            mPager.getChildAt(newPage).startAnimation(animation);

            animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,f2,
                    Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f
            );
            animation.setDuration(300);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mPager.getChildAt(oldPage).setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mPager.getChildAt(oldPage).startAnimation(animation);


        }
    }

}
