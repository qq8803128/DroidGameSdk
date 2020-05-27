package droid.game.plugin.sdk.delegate.ui.online.page.payment;

import android.view.View;
import android.widget.TextView;
import droid.game.butterknife.annotation.BindView;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.plugin.sdk.delegate.ui.online.dialog.PaymentDialog;
import droid.game.plugin.sdk.delegate.ui.online.page.Page;
import droid.game.plugin.sdk.delegate.util.AppHelper;

public class Order extends Page<PaymentDialog> {
    private PaymentDialog.PayWayInfo mPayWayInfo;

    @BindView(R3.id.droid_game_sdk_self_dialog_payment_game_name)
    TextView mGameName;

    @BindView(R3.id.droid_game_sdk_self_dialog_payment_product_name)
    TextView mProductName;

    @BindView(R3.id.droid_game_sdk_self_dialog_payment_pay_price)
    TextView mPayPrice;

    @BindView(R3.id.droid_game_sdk_self_dialog_payment_payway_name)
    TextView mPayWayName;

    public Order(View itemView, PaymentDialog dialog) {
        super(itemView, dialog);
    }

    @Override
    public Page bind() {
        setup();
        return super.bind();
    }

    public void onPayWayChanged(PaymentDialog.PayWayInfo payWayInfo){
        mPayWayInfo = payWayInfo;
        changePayWayInfo();
    }

    private void setup(){
        mPayWayInfo = getDialog().getPayWayInfo();
        changePayWayInfo();
        mGameName.setText(AppHelper.getAppName());
    }

    private void changePayWayInfo(){
        mPayWayName.setText(mPayWayInfo.name);
    }
}
