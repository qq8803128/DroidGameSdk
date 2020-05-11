package droid.game.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class UIWrapContentScrollView extends UIObservableScrollView {
    private int mMaxHeight = Integer.MAX_VALUE >> 2;

    public UIWrapContentScrollView(Context context) {
        super(context);
    }

    public UIWrapContentScrollView(Context context, int maxHeight) {
        super(context);
        mMaxHeight = maxHeight;
    }

    public UIWrapContentScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UIWrapContentScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMaxHeight(int maxHeight) {
        if (mMaxHeight != maxHeight) {
            mMaxHeight = maxHeight;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int maxHeight = Math.min(heightSize, mMaxHeight);
        int expandSpec;
        if (lp != null && lp.height > 0 && lp.height <= mMaxHeight) {
            expandSpec = View.MeasureSpec.makeMeasureSpec(lp.height, View.MeasureSpec.EXACTLY);
        } else {
            expandSpec = View.MeasureSpec.makeMeasureSpec(maxHeight, View.MeasureSpec.AT_MOST);
        }

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
