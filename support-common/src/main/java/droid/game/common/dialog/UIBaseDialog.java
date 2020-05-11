package droid.game.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import droid.game.common.dialog.widget.UIDialogRootLayout;
import droid.game.common.util.UIDisplayHelper;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class UIBaseDialog extends Dialog {
    boolean cancelable = true;
    private int featureId = Window.FEATURE_NO_TITLE;
    private int flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
    private int mask = WindowManager.LayoutParams.FLAG_FULLSCREEN;
    private Drawable backgroundDrawable = new ColorDrawable(Color.TRANSPARENT);
    private float amount = 0.3f;
    private int gravity = Gravity.CENTER;
    private int mSoftInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
    private View mContentView;
    private int mWidth = MATCH_PARENT;
    private int mHeight = MATCH_PARENT;

    public UIBaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        if (this.cancelable != cancelable) {
            this.cancelable = cancelable;
            onSetCancelable(cancelable);
        }
    }

    protected void onSetCancelable(boolean cancelable) {

    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        if (cancel && !cancelable) {
            cancelable = true;
        }
    }

    public UIBaseDialog setWindowFeature(int featureId){
        this.featureId = featureId;
        return this;
    }

    public UIBaseDialog setFlags(int flags, int mask){
        this.flags = flags;
        this.mask = mask;
        return this;
    }

    public UIBaseDialog setBackgroundDrawable(Drawable drawable){
        this.backgroundDrawable = drawable;
        return this;
    }

    public UIBaseDialog setGravity(int gravity){
        this.gravity = gravity;
        return this;
    }

    public UIBaseDialog setDimAmount(float amount){
        this.amount = amount;
        return this;
    }

    public UIBaseDialog setLayout(int w,int h){
        mWidth = w;
        mHeight = h;
        return this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        onCustomStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCustomCreate();

    }

    @Override
    public void show() {
        super.show();
        try {
            if (mContentView != null) {
                setContentView(mContentView);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    public void setFullScreen(){
        //fit dialog fullscreen style bug
        try {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = mWidth;
            lp.height = mHeight;
            onWindowAttributesChanged(lp);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    private void onCustomCreate(){
        try{
            requestWindowFeature(featureId);
            getWindow().setFlags(flags,mask);
            getWindow().setSoftInputMode(mSoftInputMode);
        }catch (Throwable e){
        }
    }

    private void onCustomStart() {
        try {
            if (getWindow() != null){
                getWindow().setDimAmount(amount);
                getWindow().setBackgroundDrawable(backgroundDrawable);
                getWindow().setGravity(gravity);
            }
        }catch (Throwable e){
        }
    }

    public void applyContentView(UIDialogRootLayout uiDialogRootLayout) {
        mContentView = uiDialogRootLayout;
    }

    public static void showSoftInputDialog(View target) {
        try {
            target.requestFocus();
            target.setFocusable(true);
            target.setFocusableInTouchMode(true);
            InputMethodManager im = (InputMethodManager) target.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.showSoftInput(target, InputMethodManager.RESULT_SHOWN);
            im.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftInputDialog(Dialog dialog) {
        try {
            View view = dialog.getCurrentFocus();
            if (view instanceof TextView) {
                InputMethodManager mInputMethodManager = (InputMethodManager) dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
