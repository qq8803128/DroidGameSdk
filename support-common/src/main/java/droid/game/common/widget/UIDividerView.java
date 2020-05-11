package droid.game.common.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class UIDividerView extends View {
    public UIDividerView(Context context) {
        super(context);
    }

    public UIDividerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UIDividerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UIDividerView setColor(int color){
        setBackgroundColor(color);
        return this;
    }

    public UIDividerView applyDivider(int actionDividerThicknes,int actionDividerStartInset,int actionDividerEndInset){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,actionDividerThicknes);
        lp.leftMargin = actionDividerStartInset;
        lp.rightMargin = actionDividerEndInset;
        setLayoutParams(lp);
        return this;
    }
}
