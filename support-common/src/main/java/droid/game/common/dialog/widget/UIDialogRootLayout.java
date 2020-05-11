package droid.game.common.dialog.widget;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import droid.game.annotation.NonNull;
import droid.game.annotation.Nullable;
import droid.game.common.util.UIDisplayHelper;
import droid.game.common.util.UIWindowHelper;

public class UIDialogRootLayout extends ViewGroup {

    private UIDialogView mDialogView;
    private FrameLayout.LayoutParams mDialogViewLp;
    private int mMinWidth;
    private int mMaxWidth;
    private int mInsetHor;
    private int mInsetVer;
    private boolean mCheckKeyboardOverlay = false;
    private float mMaxPercent = 0.75f;
    private boolean isOverlayOccurEventNotified = false;
    private OverlayOccurInMeasureCallback mOverlayOccurInMeasureCallback;
    private int mLastContentInsetTop = 0;


    public UIDialogRootLayout(@NonNull Context context, @NonNull UIDialogView dialogView,
                              @Nullable FrameLayout.LayoutParams dialogViewLp) {
        super(context);
        mDialogView = dialogView;
        if (dialogViewLp == null) {
            dialogViewLp = new FrameLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        mDialogViewLp = dialogViewLp;
        addView(mDialogView, dialogViewLp);

        mMinWidth = UIDisplayHelper.dpToPx(320);
        mMaxWidth = UIDisplayHelper.dpToPx(340);
        mInsetHor = UIDisplayHelper.dpToPx(0);
        mInsetVer = UIDisplayHelper.dpToPx(0);
    }

    public void setMinWidth(int minWidth) {
        mMinWidth = minWidth;
    }

    public void setMaxWidth(int maxWidth) {
        mMaxWidth = maxWidth;
    }

    public void setInsetHor(int insetHor) {
        mInsetHor = insetHor;
    }

    public void setInsetVer(int insetVer) {
        mInsetVer = insetVer;
    }

    public void setOverlayOccurInMeasureCallback(OverlayOccurInMeasureCallback overlayOccurInMeasureCallback) {
        mOverlayOccurInMeasureCallback = overlayOccurInMeasureCallback;
    }

    public void setCheckKeyboardOverlay(boolean checkKeyboardOverlay) {
        mCheckKeyboardOverlay = checkKeyboardOverlay;
    }

    public void setMaxPercent(float maxPercent) {
        mMaxPercent = maxPercent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int keyboardOverlayHeight = 0;
        int contentInsetVer = 0;
        if (mCheckKeyboardOverlay) {
            Rect visibleInsetRect = UIWindowHelper.unSafeGetWindowVisibleInsets(this);
            Rect contentInsetRect = UIWindowHelper.unSafeGetContentInsets(this);
            if (visibleInsetRect != null) {
                keyboardOverlayHeight = visibleInsetRect.bottom;
            }
            if (contentInsetRect != null) {
                mLastContentInsetTop = contentInsetRect.top;
                contentInsetVer = contentInsetRect.top + contentInsetRect.bottom;
            }
        }
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childWidthMeasureSpec, childHeightMeasureSpec;
        if (mDialogViewLp.width > 0) {
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mDialogViewLp.width, MeasureSpec.EXACTLY);
        } else {
            int childMaxWidth = Math.min(mMaxWidth, widthSize - 2 * mInsetHor);
            if (childMaxWidth <= mMinWidth) {
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mMinWidth, MeasureSpec.EXACTLY);
            } else if (mDialogViewLp.width == LayoutParams.MATCH_PARENT) {
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childMaxWidth, MeasureSpec.EXACTLY);
            } else {
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childMaxWidth, MeasureSpec.AT_MOST);
            }
        }

        if (mDialogViewLp.height > 0) {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mDialogViewLp.height, MeasureSpec.EXACTLY);
        } else {
            int childMaxHeight;
            if (keyboardOverlayHeight > 0) {
                if (getRootView() != null && getRootView().getHeight() > 0) {
                    // the overlay occurred with this height, we can't change it.
                    heightSize = getRootView().getHeight();
                    if (!isOverlayOccurEventNotified) {
                        isOverlayOccurEventNotified = true;
                        if (mOverlayOccurInMeasureCallback != null) {
                            mOverlayOccurInMeasureCallback.call();
                        }
                    }
                }
                childMaxHeight = Math.max(heightSize - 2 * mInsetVer - keyboardOverlayHeight - contentInsetVer, 0);
            } else {
                // use maxPercent to keep dialog from being too high and calculated based on
                // screen height because height size while change to actual height when multi onMeasure.
                isOverlayOccurEventNotified = false;
                childMaxHeight = Math.min(heightSize - 2 * mInsetVer - contentInsetVer,
                        (int) (UIDisplayHelper.getScreenHeight(getContext()) * mMaxPercent - 2 * mInsetVer));
            }
            if (mDialogViewLp.height == LayoutParams.MATCH_PARENT) {
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childMaxHeight, MeasureSpec.EXACTLY);
            } else {
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childMaxHeight, MeasureSpec.AT_MOST);
            }
        }
        mDialogView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        if (mDialogView.getMeasuredWidth() < mMinWidth) {
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mMinWidth, MeasureSpec.EXACTLY);
            mDialogView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
        // InsetVer works when keyboard overlay occurs
        setMeasuredDimension(mDialogView.getMeasuredWidth(),
                mDialogView.getMeasuredHeight() + 2 * mInsetVer + keyboardOverlayHeight + contentInsetVer);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int w = r - l;
        int childLeft = (w - mDialogView.getMeasuredWidth()) / 2;
        mDialogView.layout(childLeft, mInsetVer,
                childLeft + mDialogView.getMeasuredWidth(),
                mInsetVer + mDialogView.getMeasuredHeight());
    }

    public UIDialogView getDialogView() {
        return mDialogView;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // I think this is a android system bug:
        // When show keyboard In fullscreen and the content overlaps with keyboard,
        // then the mAttachInfo.mContentInset.top equals notch's height
        // but the event's y and draw position has different behavior if notch exist.
        if (mLastContentInsetTop > 0) {
            ev.offsetLocation(0, -mLastContentInsetTop);
        }
        return super.dispatchTouchEvent(ev);
    }

    interface OverlayOccurInMeasureCallback {
        void call();
    }
}
