package droid.game.plugin.sdk.delegate.ui.online.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import droid.game.butterknife.annotation.BindView;
import droid.game.butterknife.annotation.OnClick;
import droid.game.common.dialog.UIDialog;
import droid.game.common.dialog.widget.UIDialogRootLayout;
import droid.game.common.keep.Consumer;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.plugin.sdk.delegate.ui.online.page.Page;
import droid.game.plugin.sdk.delegate.ui.online.page.payment.Order;
import droid.game.plugin.sdk.delegate.ui.online.page.payment.PayWay;
import droid.game.plugin.sdk.delegate.util.AnimationHelper;
import droid.game.x2c.annotation.Xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Xml(layouts = {"droid_game_sdk_self_dialog_payment"})
public class PaymentDialog extends Dialog<PaymentDialog> {
    public static final int ORDER = 0;
    public static final int PAYWAY = 1;

    private List<Page> mPages = new ArrayList<>();

    @BindView(R3.id.droid_game_sdk_self_dialog_payment_page)
    ViewGroup mPager;

    @OnClick(R3.id.droid_game_sdk_self_dialog_payment_swicth_pay_way)
    void onBtnSwitchPayWayClicked(){
        showPage(PAYWAY);
    }

    @OnClick(R3.id.droid_game_sdk_self_dialog_payment_back_to_pay_page)
    void onBtnBackToPayPageClicked(){
        showPage(ORDER);
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

    @Override
    public void onAfterCreate(UIDialog dialog, UIDialogRootLayout rootLayout, Context context) {
        super.onAfterCreate(dialog, rootLayout, context);

        setup();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Page page : mPages){
            page.unbind();
        }
    }

    private void setup(){
        mPages.add(new Order(mPager,this).bind());
        mPages.add(new PayWay(mPager,this).bind());
        showPage(ORDER);
        ((PayWay)mPages.get(1)).addOnPayWayChangedListener(new Consumer<PayWayInfo>() {
            @Override
            public void accept(PayWayInfo payWayInfo) {
                ((Order)mPages.get(0)).onPayWayChanged(payWayInfo);
            }
        });
    }

    public void showPage(int newPage){
        UIDialog.hideSoftInputDialog(getDialog());
        AnimationHelper.animationToPage(mPager,newPage);
    }

    public PayWayInfo getPayWayInfo(){
        return PayWayInfo.ALIPAY;
    }

    public List<PayWayInfo> getSupportPayWayList(){
        return Arrays.asList(
                PayWayInfo.ALIPAY,
                PayWayInfo.WECHAT
        );
    }

    public static class PayWayInfo{
        public final static PayWayInfo ALIPAY = new PayWayInfo("支付宝","alipay");
        public final static PayWayInfo WECHAT = new PayWayInfo("微信支付","wechat");

        public final String name;
        public final String tag;

        public PayWayInfo(String name,String tag) {
            super();
            this.name = name;
            this.tag = tag;
        }

        public boolean equals(PayWayInfo obj) {
           return name.equals(obj.name) && tag.equals(obj.tag);
        }
    }
}
