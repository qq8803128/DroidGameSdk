package droid.game.common.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import droid.game.common.link.ITouchableSpan;
import droid.game.common.util.UIViewHelper;

public abstract class UITouchableSpan extends ClickableSpan implements ITouchableSpan {
    private static final String TAG = "QMUITouchableSpan";
    private boolean mIsPressed;
    private int mNormalBackgroundColor;
    private int mPressedBackgroundColor;
    private int mNormalTextColor;
    private int mPressedTextColor;


    private boolean mIsNeedUnderline = false;

    public abstract void onSpanClick(View widget);

    @Override
    public final void onClick(View widget) {
        if (UIViewHelper.isAttachedToWindow(widget)) {
            onSpanClick(widget);
        }
    }


    public UITouchableSpan(int normalTextColor,
                           int pressedTextColor,
                           int normalBackgroundColor,
                           int pressedBackgroundColor) {
        mNormalTextColor = normalTextColor;
        mPressedTextColor = pressedTextColor;
        mNormalBackgroundColor = normalBackgroundColor;
        mPressedBackgroundColor = pressedBackgroundColor;
    }

    public int getNormalBackgroundColor() {
        return mNormalBackgroundColor;
    }

    public void setNormalTextColor(int normalTextColor) {
        mNormalTextColor = normalTextColor;
    }

    public void setPressedTextColor(int pressedTextColor) {
        mPressedTextColor = pressedTextColor;
    }

    public int getNormalTextColor() {
        return mNormalTextColor;
    }

    public int getPressedBackgroundColor() {
        return mPressedBackgroundColor;
    }

    public int getPressedTextColor() {
        return mPressedTextColor;
    }

    @Override
    public void setPressed(boolean isSelected) {
        mIsPressed = isSelected;
    }

    public boolean isPressed() {
        return mIsPressed;
    }

    public void setIsNeedUnderline(boolean isNeedUnderline) {
        mIsNeedUnderline = isNeedUnderline;
    }

    public boolean isNeedUnderline() {
        return mIsNeedUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(mIsPressed ? mPressedTextColor : mNormalTextColor);
        ds.bgColor = mIsPressed ? mPressedBackgroundColor
                : mNormalBackgroundColor;
        ds.setUnderlineText(mIsNeedUnderline);
    }

}
