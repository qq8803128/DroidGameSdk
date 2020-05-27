package droid.game.plugin.sdk.delegate.ui.online.page.payment;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import droid.game.butterknife.annotation.BindView;
import droid.game.common.keep.Consumer;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.plugin.sdk.delegate.ui.online.dialog.PaymentDialog;
import droid.game.plugin.sdk.delegate.ui.online.page.Page;
import droid.game.plugin.sdk.delegate.util.AdapterHelper;
import droid.game.x2c.annotation.Xml;

@Xml(layouts = "droid_game_sdk_self_holder_pay_way")
public class PayWay extends Page<PaymentDialog> {
    private PaymentDialog.PayWayInfo mPayWayInfo;
    private Consumer<PaymentDialog.PayWayInfo> mOnPayWayChangedListener;

    @BindView(R3.id.droid_game_sdk_self_dialog_payment_payway_list)
    ListView mListView;

    public PayWay(View itemView, PaymentDialog dialog) {
        super(itemView, dialog);
    }

    @Override
    public Page bind() {
        setup();
        return super.bind();
    }

    public void addOnPayWayChangedListener(Consumer<PaymentDialog.PayWayInfo> onPayWayChangedListener) {
        mOnPayWayChangedListener = onPayWayChangedListener;
    }

    private void setup(){
        mPayWayInfo = getDialog().getPayWayInfo();
        configPayWayList();
    }

    private void configPayWayList(){
        mListView.setDivider(new ColorDrawable(0x75999999));
        mListView.setDividerHeight(1);
        mListView.setAdapter(new AdapterHelper.Adapter<PayWayHolder>() {
            @Override
            public PayWayHolder onCreateViewHolder(ViewGroup parent) {
                return new PayWayHolder(inflate(R3.layout.droid_game_sdk_self_holder_pay_way));
            }

            @Override
            public void onBindViewHolder(PayWayHolder holder, int position) {
                PaymentDialog.PayWayInfo payWayInfo = getDialog().getSupportPayWayList().get(position);
                holder.textView.setText(payWayInfo.name);
                if (payWayInfo.equals(mPayWayInfo)){
                    holder.textView.setTextColor(0xffff8200);
                    holder.imageView.setVisibility(View.VISIBLE);
                }else{
                    holder.textView.setTextColor(0xffafafaf);
                    holder.imageView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public int getCount() {
                return getDialog().getSupportPayWayList().size();
            }

        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PaymentDialog.PayWayInfo payWayInfo = getDialog().getSupportPayWayList().get(position);
                mPayWayInfo = payWayInfo;
                if (mOnPayWayChangedListener != null){
                    mOnPayWayChangedListener.accept(payWayInfo);
                    getDialog().showPage(PaymentDialog.ORDER);
                }
                ((BaseAdapter)mListView.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    public class PayWayHolder extends AdapterHelper.PluginViewHolder{
        @BindView(R3.id.droid_game_sdk_self_dialog_payment_holder_payway_name)
        TextView textView;

        @BindView(R3.id.droid_game_sdk_self_dialog_payment_holder_payway_check)
        ImageView imageView;

        public PayWayHolder(View itemView) {
            super(itemView);
        }
    }
}
