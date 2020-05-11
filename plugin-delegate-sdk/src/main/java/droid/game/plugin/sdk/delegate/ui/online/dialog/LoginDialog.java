package droid.game.plugin.sdk.delegate.ui.online.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import droid.game.butterknife.annotation.BindView;
import droid.game.butterknife.annotation.OnClick;
import droid.game.common.dialog.UIDialog;
import droid.game.common.dialog.widget.UIDialogRootLayout;
import droid.game.common.span.Span;
import droid.game.common.widget.LoadingView;
import droid.game.common.widget.UISpanTouchFixTextView;
import droid.game.plugin.sdk.delegate.R3;
import droid.game.plugin.sdk.delegate.ui.online.BaseUi;
import droid.game.x2c.annotation.Xml;

import static droid.game.common.util.UIDisplayHelper.dpToPx;

@Xml(layouts = {"droid_game_sdk_self_dialog_login","droid_game_sdk_self_holder_account"})
public class LoginDialog extends BaseUi<LoginDialog> {

    final int LOGIN_SELECT = 0;
    final int LOGIN_NAME = 1;
    final int LOGIN_MOBILE = 2;
    final int REGISTER_MOBILE = 3;
    final int REGISTER_NAME = 4;
    final int MISS_PASSWORD = 5;

    private int mFirstPage = LOGIN_NAME;
    private int mCurrentPage;
    private boolean mIsQuickLogin = false;

    @BindView(R3.id.droid_game_sdk_self_account_select_list)
    ListView mListView;

    @BindView(R3.id.droid_game_sdk_self_dialog_login_page)
    ViewGroup mPager;

    @BindView(R3.id.droid_game_sdk_self_dialog_loading_page)
    ViewGroup mLoading;

    @BindView(R3.id.droid_game_sdk_self_open_name_register_from_mobile_register)
    UISpanTouchFixTextView mOpenNameLogin;

    @BindView(R3.id.droid_game_sdk_self_register_name_user_protocol)
    UISpanTouchFixTextView mNameUserProtocol;

    @BindView(R3.id.droid_game_sdk_self_register_mobile_user_protocol)
    UISpanTouchFixTextView mMobileUserProtocol;

    @OnClick(R3.id.droid_game_sdk_self_account_select_other_account)
    void onBtnSelectOtherAccountClicked(){
        showPage(LOGIN_NAME);
    }

    @OnClick(R3.id.droid_game_sdk_self_open_mobile_login_page)
    void onBtnOpenMobileLoginPageClicked(){
        showPage(LOGIN_MOBILE);
    }

    @OnClick(R3.id.droid_game_sdk_self_open_quick_login_page)
    void onBtnOpenQuickLoginPageClicked(){
        mIsQuickLogin = true;
        showPage(REGISTER_NAME);
    }

    @OnClick({R3.id.droid_game_sdk_self_open_mobile_register_page,R3.id.droid_game_sdk_self_open_mobile_register_from_name_register})
    void onBtnOpenMobileRegisterPageClicked(){
        showPage(REGISTER_MOBILE);
    }

    @OnClick({R3.id.droid_game_sdk_self_open_name_login_from_mobile_login,R3.id.droid_game_sdk_self_open_name_login_from_mobile_register,R3.id.droid_game_sdk_self_open_name_login_from_miss_password})
    void onBtnBackToNameLoginPageFromMobileLoginClicked(){
        showPage(LOGIN_NAME);
    }

    @OnClick(R3.id.droid_game_sdk_self_open_name_register_from_mobile_register)
    void onBtnOpenNameLoginPageClicked(){
        mIsQuickLogin = false;
        showPage(REGISTER_NAME);
    }

    @OnClick(R3.id.droid_game_sdk_self_open_miss_password)
    void onBtnOpenMissPasswordPageClicked(){
        showPage(MISS_PASSWORD);
    }

    @OnClick(R3.id.droid_game_sdk_self_do_name_login)
    void onBtnDoNameLoginClicked(){
        getDialog().dismiss();
    }

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

        new AutoLoginDialog(dialog.getContext()).create()
                .addOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mPager.setVisibility(View.VISIBLE);
                        mLoading.setVisibility(View.GONE);
                        showFirstPage();
                    }
                })
                .show();
    }

    private void setup(){
        interceptBackPressed();
        fillSpanText();

        ((LoadingView)((ViewGroup)mLoading.getChildAt(0)).getChildAt(0)).setLoadingColor(0xffafafaf);
    }

    private void showFirstPage(){
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
                showPage(mIsQuickLogin ? LOGIN_NAME : REGISTER_MOBILE);
                mIsQuickLogin = false;
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

    private void fillSpanText(){
        Span span = new Span("(不推荐)用户名注册").add();
        span.lightText("(不推荐)").textColor(0xffa0a0a0,0xffa0a0a0);
        mOpenNameLogin.setMovementMethodDefault();
        mOpenNameLogin.setText(span.build());

        span = new Span("注册即同意《用户协议》").add();
        span.lightText("《用户协议》").textColor(0xffff8200,0xffff8200).listener(new Span.OnClickSpanListener() {
                    @Override
                    public void onClick(View v, String lightText) {
                        openUserProtocolPage();
                    }
                });
        CharSequence charSequence = span.build();
        mMobileUserProtocol.setMovementMethodDefault();
        mMobileUserProtocol.setText(charSequence);

        span = new Span("注册即同意《用户协议》").add();
        span.lightText("《用户协议》").textColor(0xffff8200,0xffff8200).listener(new Span.OnClickSpanListener() {
                    @Override
                    public void onClick(View v, String lightText) {
                        openUserProtocolPage();
                    }
                });
        charSequence = span.build();
        mNameUserProtocol.setMovementMethodDefault();
        mNameUserProtocol.setText(charSequence);
    }

    private void fillAccountList(){
        mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mListView.setDividerHeight(dpToPx(8));
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null){
                    convertView = inflate(R3.layout.droid_game_sdk_self_holder_account);
                }
                return convertView;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void showPage(int newPage){
        mCurrentPage = newPage;

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

    private void openUserProtocolPage(){

    }
}
