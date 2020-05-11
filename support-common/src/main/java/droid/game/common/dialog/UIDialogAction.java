package droid.game.common.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import droid.game.common.util.UIDisplayHelper;
import droid.game.common.widget.UIButton;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class UIDialogAction {
    boolean mIsSeparator;
    private int mSeparatorEndInset;
    private int mSeparatorStartInset;
    private int mSeparatorThickness;
    private int mSeparatorColor;
    private ActionListener mListener;
    private String mText;
    private int mTextColor;
    private boolean mBold;

    public UIDialogAction(String text, int textColor, boolean bold,ActionListener listener) {
        mIsSeparator = false;
        setText(text);
        setTextColor(textColor);
        setActionListener(listener);
        setBold(bold);
    }

    public UIDialogAction(int color, int thickness, int startInset, int endInset){
        mIsSeparator = true;

        mSeparatorColor = color;
        mSeparatorThickness = thickness;
        mSeparatorStartInset = startInset;
        mSeparatorEndInset = endInset;
    }

    public UIDialogAction setText(String text){
        mText = text;
        return this;
    }

    public UIDialogAction setTextColor(int textColor){
        mTextColor = textColor;
        return this;
    }

    public UIDialogAction setBold(boolean bold){
        mBold = bold;
        return this;
    }

    public UIDialogAction setActionListener(ActionListener listener){
        mListener = listener;
        return this;
    }

    public View build(Context context,int orientation){
        View view = null;
        if (mIsSeparator){
            view = buildSeparatorView(context,orientation);
        }else{
            view = buildActionView(context,orientation);
        }
        return view;
    }

    protected View buildSeparatorView(Context context,int orientation){
        int w = orientation == LinearLayout.VERTICAL ? MATCH_PARENT : (mSeparatorThickness - mSeparatorStartInset - mSeparatorEndInset);
        int h = orientation == LinearLayout.VERTICAL ? (mSeparatorThickness - mSeparatorStartInset - mSeparatorEndInset) : MATCH_PARENT;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w,h);
        if (LinearLayout.VERTICAL == orientation){
            lp.leftMargin = mSeparatorStartInset;
            lp.rightMargin = mSeparatorEndInset;
        }else{
            lp.topMargin = mSeparatorStartInset;
            lp.bottomMargin = mSeparatorEndInset;
        }
        View view = new View(context);
        view.setBackgroundColor(mSeparatorColor);
        view.setLayoutParams(lp);
        return view;
    }

    protected View buildActionView(Context context,int orientation){
        int w = orientation == LinearLayout.VERTICAL ? MATCH_PARENT : 0;
        int h = orientation == LinearLayout.VERTICAL ? UIDisplayHelper.dpToPx(48) : MATCH_PARENT;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w,h);
        if (orientation == LinearLayout.HORIZONTAL){
            lp.weight = 1;
        }
        UIButton uiButton = new UIButton(context);
        uiButton.setChangeAlphaWhenDisable(true);
        uiButton.setChangeAlphaWhenPress(true);
        uiButton.setLayoutParams(lp);
        uiButton.setTextSize(15);
        uiButton.getPaint().setFakeBoldText(mBold);
        uiButton.setBackgroundColor(Color.TRANSPARENT);
        uiButton.setText(mText);
        uiButton.setTextColor(mTextColor);
        uiButton.setGravity(Gravity.CENTER);

        return uiButton;
    }

    public ActionListener getListener() {
        return mListener;
    }

    public interface ActionListener {
        void onClick(UIDialog dialog);
    }

}
