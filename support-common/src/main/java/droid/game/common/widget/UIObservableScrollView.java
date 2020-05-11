package droid.game.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class UIObservableScrollView extends ScrollView {

    private int mScrollOffset = 0;

    private List<OnScrollChangedListener> mOnScrollChangedListeners;

    public UIObservableScrollView(Context context) {
        super(context);
    }

    public UIObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UIObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        if (mOnScrollChangedListeners == null) {
            mOnScrollChangedListeners = new ArrayList<>();
        }
        if (mOnScrollChangedListeners.contains(onScrollChangedListener)) {
            return;
        }
        mOnScrollChangedListeners.add(onScrollChangedListener);
    }

    public void removeOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        if (mOnScrollChangedListeners == null) {
            return;
        }
        mOnScrollChangedListeners.remove(onScrollChangedListener);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mScrollOffset = t;
        if (mOnScrollChangedListeners != null && !mOnScrollChangedListeners.isEmpty()) {
            for (OnScrollChangedListener listener : mOnScrollChangedListeners) {
                listener.onScrollChanged(this, l, t, oldl, oldt);
            }
        }
    }

    public int getScrollOffset() {
        return mScrollOffset;
    }

    public interface OnScrollChangedListener {
        void onScrollChanged(UIObservableScrollView scrollView, int l, int t, int oldl, int oldt);
    }

}
