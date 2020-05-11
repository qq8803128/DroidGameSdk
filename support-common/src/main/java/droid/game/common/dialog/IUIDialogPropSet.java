package droid.game.common.dialog;

import android.graphics.drawable.Drawable;
import droid.game.annotation.FloatRange;

public interface IUIDialogPropSet<T extends UIDialogBuilder> {
    T setCancelable(boolean cancelable);
    T setCanceledOnTouchOutside(boolean canceledOnTouchOutside);
    T setLayout(int minWidth,int maxWidth,int insetHor,int insetVer);
    T setPercent(@FloatRange(from = 0.0f,to = 1.0f) float percent);
    T setWindowFeature(int feature);
    T setFlags(int flags, int mask);
    T setBackgroundDrawable(Drawable drawable);
    T setGravity(int gravity);
    T setDimAmount(float amount);
    T setStyle(int style);

    T setTitle(String title);

    T setActionContainerOrientation(int actionContainerOrientation);
    T setActionContainerGravity(int actionContainerGravity);
    T setActionContainerDivider(int actionDividerColor,int actionDividerThickness, int actionDividerStartInset, int actionDividerEndInset);
    T setCheckKeyboardOverlay(boolean checkKeyboardOverlay);

    T addAction(String text);
    T addAction(String text,boolean bold);
    T addAction(String text,int textColor);
    T addAction(String text,int textColor,boolean bold);
    T addAction(String text,boolean bold,UIDialogAction.ActionListener listener);
    T addAction(String text, UIDialogAction.ActionListener listener);
    T addAction(String text,int textColor,UIDialogAction.ActionListener listener);
    T addAction(String text,int textColor,boolean bold,UIDialogAction.ActionListener listener);
    T addAction(UIDialogAction action);

    T addSeparatorAction(int color,int thickness);
    T addSeparatorAction(int color,int thickness, int startInset, int endInset);
}
