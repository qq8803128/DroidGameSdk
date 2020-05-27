package droid.game.plugin.sdk.delegate.ui.online.page.login;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import droid.game.butterknife.annotation.BindView;
import droid.game.butterknife.annotation.OnClick;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.plugin.sdk.delegate.db.entity.AccountEntity;
import droid.game.plugin.sdk.delegate.ui.online.dialog.LoginDialog;
import droid.game.plugin.sdk.delegate.ui.online.page.Page;
import droid.game.plugin.sdk.delegate.util.AdapterHelper;
import droid.game.x2c.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

import static droid.game.common.util.UIDisplayHelper.dpToPx;

@Xml(layouts = "droid_game_sdk_self_holder_account")
public class LoginSwitch extends Page<LoginDialog> {
    private List<AccountEntity> mAccounts = new ArrayList<>();

    @BindView(R3.id.droid_game_sdk_self_account_select_list)
    ListView mListView;

    @OnClick(R3.id.droid_game_sdk_self_account_select_other_account)
    void onBtnSelectOtherAccountClicked(){
        getDialog().showPage(LoginDialog.LOGIN_NAME);
    }

    public LoginSwitch(View itemView, LoginDialog dialog) {
        super(itemView,dialog);
    }

    @Override
    protected void initialize() {
        super.initialize();
        configAccountList();
    }

    private void configAccountList(){
        mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mListView.setDividerHeight(dpToPx(8));
        mListView.setAdapter(new AdapterHelper.Adapter<AccountHolder>() {
            @Override
            public AccountHolder onCreateViewHolder(ViewGroup parent) {
                return new AccountHolder(inflate(R3.layout.droid_game_sdk_self_holder_account));
            }

            @Override
            public void onBindViewHolder(AccountHolder holder, int position) {

            }

            @Override
            public int getCount() {
                return mAccounts.size();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private class AccountHolder extends AdapterHelper.PluginViewHolder{
        public AccountHolder(View itemView) {
            super(itemView);
        }
    }
}
