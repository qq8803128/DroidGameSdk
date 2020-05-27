package droid.game.plugin.sdk.delegate.ui.online.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import droid.game.butterknife.annotation.BindView;
import droid.game.common.dialog.UIDialog;
import droid.game.common.dialog.widget.UIDialogRootLayout;
import droid.game.open.source.orm.db.assit.QueryBuilder;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.plugin.sdk.delegate.db.LiteOrmImpl;
import droid.game.plugin.sdk.delegate.db.entity.AccountEntity;
import droid.game.plugin.sdk.delegate.ui.online.page.Page;
import droid.game.plugin.sdk.delegate.ui.online.page.login.*;
import droid.game.plugin.sdk.delegate.util.AnimationHelper;
import droid.game.x2c.annotation.Xml;


import java.util.ArrayList;
import java.util.List;

@Xml(layouts = "droid_game_sdk_self_dialog_login")
public class LoginDialog extends Dialog<LoginDialog> {

    public static final int LOGIN_SELECT = 0;
    public static final int LOGIN_NAME = 1;
    public static final int LOGIN_MOBILE = 2;
    public static final int REGISTER_MOBILE = 3;
    public static final int REGISTER_NAME = 4;
    public static final int MISS_PASSWORD = 5;

    private int mFirstPage = LOGIN_NAME;
    private int mCurrentPage;
    private boolean mIsQuickRegister = false;
    private List<AccountEntity> mAccounts = new ArrayList<>();
    private List<Page> mPages = new ArrayList<>();


    @BindView(R3.id.droid_game_sdk_self_dialog_login_page)
    ViewGroup mPager;

    public LoginDialog(Context context) {
        super(context);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected String getLayout() {
        return R3.layout.droid_game_sdk_self_dialog_login;
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
        mPages.add(new LoginSwitch(mPager,this).bind());
        mPages.add(new LoginName(mPager,this).bind());
        mPages.add(new LoginPhone(mPager,this).bind());
        mPages.add(new RegisterPhone(mPager,this).bind());
        mPages.add(new RegisterName(mPager,this).bind());
        mPages.add(new MissPassword(mPager,this).bind());

        interceptBackPressed();
        showFirstPage();
    }

    private void showFirstPage(){
        mAccounts = LiteOrmImpl.getInstance().getLiteOrm()
                .query(
                        QueryBuilder
                                .create(AccountEntity.class)
                                .appendOrderDescBy("timestamp")
                );

        if (mAccounts == null || mAccounts.size() == 0){
            mFirstPage = LOGIN_NAME;
        }else{
            mFirstPage = LOGIN_SELECT;
        }

        //hide loading layout
        ((ViewGroup)mPager.getParent()).getChildAt(0).setVisibility(View.VISIBLE);
        ((ViewGroup)mPager.getParent()).getChildAt(1).setVisibility(View.GONE);

        showPage(mFirstPage);
    }

    private boolean onBackPressed(){
        if (mPager.getVisibility() != View.VISIBLE){
            return true;
        }

        if (mFirstPage != mCurrentPage) {
            goBack();
            return true;
        }
        return false;
    }

    private void goBack(){
        switch (mCurrentPage){
            case LOGIN_MOBILE:
                showPage(LOGIN_NAME);
                break;
            case LOGIN_NAME:
                showPage(LOGIN_SELECT);
                break;
            case LOGIN_SELECT:
                break;
            case REGISTER_MOBILE:
                showPage(LOGIN_NAME);
                break;
            case REGISTER_NAME:
                showPage(mIsQuickRegister ? LOGIN_NAME : REGISTER_MOBILE);
                mIsQuickRegister = false;
                break;
            case MISS_PASSWORD:
                showPage(LOGIN_NAME);
                break;
        }
    }

    private void interceptBackPressed(){
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    if (event.getAction() == KeyEvent.ACTION_UP){
                        return onBackPressed();
                    }
                    return false;
                }
                return false;
            }
        });
    }

    public void showPage(int newPage){
        mCurrentPage = newPage;
        UIDialog.hideSoftInputDialog(getDialog());
        AnimationHelper.animationToPage(mPager,newPage);
    }

    public void setQuickRegister(boolean b) {
        mIsQuickRegister = b;
    }
}
