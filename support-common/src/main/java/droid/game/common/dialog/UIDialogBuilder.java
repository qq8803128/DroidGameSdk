package droid.game.common.dialog;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import droid.game.common.dialog.widget.UIDialogRootLayout;
import droid.game.common.dialog.widget.UIDialogView;
import droid.game.common.util.UIDisplayHelper;
import droid.game.common.widget.UIDividerView;
import droid.game.common.widget.UISpanTouchFixTextView;
import droid.game.common.widget.UIWrapContentScrollView;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public abstract class UIDialogBuilder<T extends UIDialogBuilder> implements IUIDialogCreator,IUIDialogPropSet<T>,IContentScroll{
    private final int DEF_ACTION_TEXT_COLOR = Color.parseColor("#3478f6");

    private Context mContext;
    private UIDialog mDialog;
    private UIDialogView mDialogView;
    private UIDialogRootLayout mRootLayout;

    private int mStyle = android.R.style.Theme_Dialog;
    private boolean mCancelable = true;
    private boolean mCanceledOnTouchOutside = true;
    private int mMinWidth = 0;
    private int mMaxWidth = 0;
    private int mInsetHor = 0;
    private int mInsetVer = 0;
    private float mPercent = 0.75f;
    private int mFeature = Window.FEATURE_NO_TITLE;
    private int mFlags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
    private int mMask = WindowManager.LayoutParams.FLAG_FULLSCREEN;
    private Drawable mDrawable = new ColorDrawable(Color.TRANSPARENT);
    private int mGravity = Gravity.CENTER;
    private float mAmount = 0.5f;

    private String mTitle;
    private int mActionContainerOrientation = LinearLayout.HORIZONTAL;
    private int mActionContainerGravity;
    private int mActionDividerColor;
    private int mActionDividerThickness;
    private int mActionDividerStartInset;
    private int mActionDividerEndInset;
    private boolean mCheckKeyboardOverlay = false;
    private List<UIDialogAction> mActions = new ArrayList<>();

    public UIDialogBuilder(Context context) {
        super();
        mContext = context;
        mMinWidth = UIDisplayHelper.dpToPx(320);
        mMaxWidth = UIDisplayHelper.dpToPx(340);
        mInsetVer = UIDisplayHelper.dpToPx(0);
        mInsetHor = UIDisplayHelper.dpToPx(0);
    }

    public Context getBaseContext(){
        return mContext;
    }

    public UIDialog getDialog(){
        return mDialog;
    }

    public void show(){
        create().show();
    }

    @Override
    public UIDialog create() {
        final Context context = getBaseContext();

        UIDialog uiDialog = onCreateDialog(context);
        mDialog = uiDialog;
        configDialog(uiDialog);

        UIDialogView dialogView = onCreateDialogView(context);
        configDialogView(dialogView);

        final UIDialogRootLayout uiDialogRootLayout = onCreateDialogRootLayout(context);
        configDialogRootLayout(uiDialogRootLayout);

        View titleView = onCreateTitleView(uiDialog,dialogView,context);
        View contentView = onCreateContentView(uiDialog,dialogView,context);
        View operatorView = onCreateOperatorView(uiDialog,dialogView,context);

        LinearLayout.LayoutParams lp = null;

        if (hasTitle() && (lp = onCreateTitleViewParams(context)) != null && titleView != null){
            dialogView.addView(titleView, lp);
            configTitleView(titleView);
        }

        if ((lp = onCreateContentViewParams(context)) != null && contentView != null){
            dialogView.addView(contentView, lp);
        }

        if ((lp = onCreateOperatorViewParams(context)) != null && operatorView != null){
            dialogView.addView(operatorView, lp);
            configOperatorView(operatorView);
        }

        //setContentView必须在show之后调用 否则无法因此dialog的titleBar
        //因此添加一个新的设置contentView方法
        uiDialog.applyContentView(uiDialogRootLayout);

        configDialog(uiDialog);

        uiDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                onAfterCreate(mDialog, uiDialogRootLayout, context);
            }
        });

        return uiDialog;
    }

    protected boolean hasTitle(){
        return !textEmpty(mTitle);
    }

    @Override
    public UIDialog onCreateDialog(Context context) {
        return new UIDialog(context,mStyle);
    }

    @Override
    public void configDialog(UIDialog dialog) {
        dialog.setCancelable(mCancelable);
        dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
        dialog.setWindowFeature(mFeature);
        dialog.setFlags(mFlags,mMask);
        dialog.setBackgroundDrawable(mDrawable);
        dialog.setGravity(mGravity);
        dialog.setDimAmount(mAmount);
    }

    @Override
    public UIDialogView onCreateDialogView(Context context) {
        mDialogView = new UIDialogView(context);
        return mDialogView;
    }

    @Override
    public FrameLayout.LayoutParams onCreateDialogViewLayoutParam() {
        return new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void configDialogView(UIDialogView dialogView) {
        dialogView.setOrientation(LinearLayout.VERTICAL);
    }

    @Override
    public UIDialogRootLayout onCreateDialogRootLayout(Context context) {
        mRootLayout = new UIDialogRootLayout(context,mDialogView,onCreateDialogViewLayoutParam());
        return mRootLayout;
    }

    @Override
    public void configDialogRootLayout(UIDialogRootLayout dialogRootLayout) {
        dialogRootLayout.setMaxPercent(mPercent);
        dialogRootLayout.setInsetHor(mInsetHor);
        dialogRootLayout.setInsetVer(mInsetVer);
        dialogRootLayout.setMaxWidth(mMaxWidth);
        dialogRootLayout.setMinWidth(mMinWidth);
        dialogRootLayout.setCheckKeyboardOverlay(mCheckKeyboardOverlay);
    }

    @Override
    public View onCreateTitleView(UIDialog dialog, UIDialogView dialogView, Context context) {
        if (hasTitle()) {
            TextView tv = new UISpanTouchFixTextView(context);
            tv.setText(mTitle);
            return tv;
        }
        return null;
    }

    @Override
    public LinearLayout.LayoutParams onCreateTitleViewParams(Context context) {
        return new LinearLayout.LayoutParams(MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void configTitleView(View titleView) {
        if (titleView != null && titleView instanceof UISpanTouchFixTextView){
            UISpanTouchFixTextView spanTouchFixTextView = (UISpanTouchFixTextView) titleView;
            spanTouchFixTextView.setTextColor(Color.BLACK);
            spanTouchFixTextView.setTextSize(16.5f);
            spanTouchFixTextView.setGravity(Gravity.CENTER);
            spanTouchFixTextView.setSingleLine();
            spanTouchFixTextView.setPadding(UIDisplayHelper.dpToPx(16), UIDisplayHelper.dpToPx(24), UIDisplayHelper.dpToPx(16), UIDisplayHelper.dpToPx(0));
        }
    }

    @Override
    public abstract View onCreateContentView(UIDialog dialog, UIDialogView dialogView, Context context);

    @Override
    public LinearLayout.LayoutParams onCreateContentViewParams(Context context) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MATCH_PARENT, 0);
        lp.weight = 1;
        return lp;
    }

    @Override
    public View onCreateOperatorView(UIDialog dialog, UIDialogView dialogView, Context context) {
        return new LinearLayout(context);
    }

    @Override
    public LinearLayout.LayoutParams onCreateOperatorViewParams(Context context) {
        return new LinearLayout.LayoutParams(MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void configOperatorView(View operatorView) {
        if (mActions == null || mActions.size() == 0){
            return;
        }

        if (operatorView instanceof LinearLayout){
            LinearLayout linearLayout = (LinearLayout) operatorView;
            linearLayout.setGravity(LinearLayout.VERTICAL);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout actionLayout = new LinearLayout(operatorView.getContext());
            linearLayout.addView(
                    new UIDividerView(getBaseContext())
                            .setColor(mActionDividerColor)
                            .applyDivider(mActionDividerThickness,mActionDividerStartInset,mActionDividerEndInset)

            );
            linearLayout.addView(actionLayout,MATCH_PARENT,WRAP_CONTENT);

            actionLayout.setGravity(mActionContainerGravity);
            actionLayout.setOrientation(mActionContainerOrientation);

            if (mActionContainerGravity == LinearLayout.HORIZONTAL){
                actionLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, UIDisplayHelper.dpToPx(48)));
            }

            for (final UIDialogAction action : mActions){
                View view = action.build(getBaseContext(),mActionContainerOrientation);
                actionLayout.addView(view);
                if (action.getListener() != null){
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            action.getListener().onClick(getDialog());
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onAfterCreate(UIDialog dialog, UIDialogRootLayout rootLayout, Context context) {

    }

    @Override
    public T setCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return (T) this;
    }

    @Override
    public T setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        mCanceledOnTouchOutside = canceledOnTouchOutside;
        return (T) this;
    }

    @Override
    public T setLayout(int minWidth, int maxWidth, int insetHor, int insetVer) {
        mMinWidth = minWidth;
        mMaxWidth = maxWidth;
        mInsetHor = insetHor;
        mInsetVer = insetVer;
        return (T) this;
    }

    @Override
    public T setPercent(float percent) {
        mPercent = percent;
        return (T) this;
    }

    @Override
    public T setWindowFeature(int feature) {
        mFeature = feature;
        return (T) this;
    }

    @Override
    public T setFlags(int flags, int mask) {
        mFlags = flags;
        mMask = mask;
        return (T) this;
    }

    @Override
    public T setBackgroundDrawable(Drawable drawable) {
        mDrawable = drawable;
        return (T) this;
    }

    @Override
    public T setGravity(int gravity) {
        mGravity = gravity;
        return (T) this;
    }

    @Override
    public T setDimAmount(float amount) {
        mAmount = amount;
        return (T) this;
    }

    @Override
    public T setStyle(int style) {
        mStyle = style;
        return (T) this;
    }

    @Override
    public T setTitle(String title) {
        mTitle = title;
        return (T) this;
    }

    @Override
    public T setActionContainerOrientation(int actionContainerOrientation) {
        mActionContainerOrientation = actionContainerOrientation;
        return (T) this;
    }

    @Override
    public T setActionContainerGravity(int actionGravity) {
        mActionContainerGravity = actionGravity;
        return (T) this;
    }

    @Override
    public T setActionContainerDivider(int color, int thickness, int startInset, int endInset) {
        mActionDividerColor = color;
        mActionDividerThickness = thickness;
        mActionDividerStartInset = startInset;
        mActionDividerEndInset = endInset;
        return (T) this;
    }

    @Override
    public T setCheckKeyboardOverlay(boolean checkKeyboardOverlay) {
        mCheckKeyboardOverlay  = checkKeyboardOverlay;
        return (T) this;
    }

    @Override
    public T addAction(String text) {
        return addAction(text,DEF_ACTION_TEXT_COLOR);
    }

    @Override
    public T addAction(String text,boolean bold) {
        return addAction(text,DEF_ACTION_TEXT_COLOR,bold);
    }


    @Override
    public T addAction(String text, int textColor) {
        return addAction(text,textColor,null);
    }

    @Override
    public T addAction(String text, int textColor, boolean bold) {
        return addAction(text,textColor,bold,null);
    }

    @Override
    public T addAction(String text, boolean bold, UIDialogAction.ActionListener listener) {
        return addAction(text,DEF_ACTION_TEXT_COLOR,bold,listener);
    }

    @Override
    public T addAction(String text, UIDialogAction.ActionListener listener) {
        return addAction(text,DEF_ACTION_TEXT_COLOR,listener);
    }

    @Override
    public T addAction(String text, int textColor, UIDialogAction.ActionListener listener) {
        return addAction(text,textColor,false,listener);
    }

    @Override
    public T addAction(String text, int textColor, boolean bold, UIDialogAction.ActionListener listener) {
        return addAction(new UIDialogAction(text,textColor,bold,listener));
    }

    @Override
    public T addAction(UIDialogAction action) {
        if (!mActions.contains(action)){
            mActions.add(action);
        }
        return (T) this;
    }

    @Override
    public T addSeparatorAction(int color, int thickness) {
        return addSeparatorAction(color,thickness,0,0);
    }

    @Override
    public T addSeparatorAction(int color, int thickness, int startInset, int endInset) {
        return addAction(new UIDialogAction(color,thickness,startInset,endInset));
    }

    @Override
    public UIWrapContentScrollView wrapWithScroll(View view) {
        UIWrapContentScrollView scrollView = new UIWrapContentScrollView(view.getContext());
        scrollView.addView(view);
        scrollView.setVerticalScrollBarEnabled(false);
        return scrollView;
    }

    protected boolean textEmpty(String text){
        return text == null || text.length() == 0;
    }

    protected boolean textEmpty(CharSequence charSequence){
        return charSequence == null || charSequence.length() == 0;
    }

}
