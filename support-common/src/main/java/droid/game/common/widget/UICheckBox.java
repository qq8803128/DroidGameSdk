package droid.game.common.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Checkable;

import static droid.game.common.util.UIDisplayHelper.dpToPx;

public class UICheckBox extends View implements Checkable {
    private final double mSin27 = Math.sin(Math.toRadians(27));
    private final double mSin63 = Math.sin(Math.toRadians(63));
    private final int mDuration = 500;
    private final int defaultSize = 40;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int radius;
    private RectF mRectF = new RectF();
    private RectF mInnerRectF = new RectF();
    private Path mPath = new Path();
    private float mSweepAngle;
    private float mHookStartY;
    private float mBaseLeftHookOffset;
    private float mBaseRightHookOffset;
    private float mEndLeftHookOffset;
    private float mEndRightHookOffset;
    private int size;
    private boolean mChecked = true;
    private float mHookOffset;
    private float mHookSize;
    private int mInnerCircleAlpha = 0XFF;
    private int mStrokeWidth = dpToPx(2);
    private int mStrokeColor = Color.WHITE;
    private int mCircleColor = Color.WHITE;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public UICheckBox(Context context) {
        this(context, null);
    }

    public UICheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UICheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST &&
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();

            width = height = Math.min(dip(defaultSize) - params.leftMargin - params.rightMargin,
                    dip(defaultSize) - params.bottomMargin - params.topMargin);
        }
        int size = Math.min(width - getPaddingLeft() - getPaddingRight(),
                height - getPaddingBottom() - getPaddingTop());
        setMeasuredDimension(size, size);
    }
    */

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        size = getWidth();
        radius = (getWidth() - (2 * mStrokeWidth)) / 2;
        mRectF.set(mStrokeWidth, mStrokeWidth, size - mStrokeWidth, size - mStrokeWidth);
        mInnerRectF.set(mRectF);
        mInnerRectF.inset(mStrokeWidth / 2, mStrokeWidth / 2);
        mHookStartY = (float) (size / 2 - (radius * mSin27 + (radius - radius * mSin63)));
        mBaseLeftHookOffset = (float) (radius * (1 - mSin63)) + mStrokeWidth / 2;
        mBaseRightHookOffset = 0f;
        mEndLeftHookOffset = mBaseLeftHookOffset + (2 * size / 3 - mHookStartY) * 0.33f;
        mEndRightHookOffset = mBaseRightHookOffset + (size / 3 + mHookStartY) * 0.38f;
        mHookSize = size - (mEndLeftHookOffset + mEndRightHookOffset);
        mHookOffset = mChecked ? mHookSize + mEndLeftHookOffset - mBaseLeftHookOffset : 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        drawHook(canvas);
    }

    private int mColor = 0xffF3721E;

    public void setColor(int color) {
        mColor = color;
        //invalidate();
    }

    private void drawCircle(Canvas canvas) {
        initDrawStrokeCirclePaint();
        //canvas.drawArc(mRectF, 202, mSweepAngle, false, mPaint);
        //initDrawAlphaStrokeCirclePaint();
        //canvas.drawArc(mRectF, 202, mSweepAngle - 360, false, mPaint);
        //initDrawInnerCirclePaint();
        //canvas.drawArc(mInnerRectF, 0, 360, false, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);
        canvas.drawRoundRect(
                new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), dpToPx(2.5f), dpToPx(2.5f), mPaint
        );
    }

    private void drawHook(Canvas canvas) {
        if (mHookOffset == 0) {
            return;
        }
        initDrawHookPaint();
        mPath.reset();
        float offset;
        if (mHookOffset <= (2 * size / 3 - mHookStartY - mBaseLeftHookOffset)) {
            mPath.moveTo(mBaseLeftHookOffset, mBaseLeftHookOffset + mHookStartY);
            mPath.lineTo(mBaseLeftHookOffset + mHookOffset, mBaseLeftHookOffset + mHookStartY + mHookOffset);
        } else if (mHookOffset <= mHookSize) {
            mPath.moveTo(mBaseLeftHookOffset, mBaseLeftHookOffset + mHookStartY);
            mPath.lineTo(2 * size / 3 - mHookStartY, 2 * size / 3);
            mPath.lineTo(mHookOffset + mBaseLeftHookOffset,
                    2 * size / 3 - (mHookOffset - (2 * size / 3 - mHookStartY - mBaseLeftHookOffset)));
        } else {
            offset = mHookOffset - mHookSize;
            mPath.moveTo(mBaseLeftHookOffset + offset, mBaseLeftHookOffset + mHookStartY + offset);
            mPath.lineTo(2 * size / 3 - mHookStartY, 2 * size / 3);
            mPath.lineTo(mHookSize + mBaseLeftHookOffset + offset,
                    2 * size / 3 - (mHookSize - (2 * size / 3 - mHookStartY - mBaseLeftHookOffset) + offset));
        }
        canvas.drawPath(mPath, mPaint);
    }

    private void initDrawHookPaint() {
        mPaint.setAlpha(0xFF);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mStrokeColor);
    }

    private void initDrawStrokeCirclePaint() {
        mPaint.setAlpha(0xFF);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mStrokeColor);
    }

    private void initDrawAlphaStrokeCirclePaint() {
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mStrokeColor);
        mPaint.setAlpha(0x40);
    }

    private void initDrawInnerCirclePaint() {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleColor);
        mPaint.setAlpha(mInnerCircleAlpha);
    }

    private void startCheckedAnim() {
        ValueAnimator animator = new ValueAnimator();
        final float hookMaxValue = mHookSize + mEndLeftHookOffset - mBaseLeftHookOffset;
        final float circleMaxFraction = mHookSize / hookMaxValue;
        final float circleMaxValue = 360 / circleMaxFraction;
        animator.setFloatValues(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                mHookOffset = fraction * hookMaxValue;
                if (fraction <= circleMaxFraction) {
                    mSweepAngle = (int) ((circleMaxFraction - fraction) * circleMaxValue);
                } else {
                    mSweepAngle = 0;
                }
                mInnerCircleAlpha = (int) (fraction * 0xFF);
                invalidate();
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(mDuration).start();
    }

    private void startUnCheckedAnim() {
        ValueAnimator animator = new ValueAnimator();
        final float hookMaxValue = mHookSize + mEndLeftHookOffset - mBaseLeftHookOffset;
        final float circleMinFraction = (mEndLeftHookOffset - mBaseLeftHookOffset) / hookMaxValue;
        final float circleMaxValue = 360 / (1 - circleMinFraction);
        animator.setFloatValues(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float circleFraction = animation.getAnimatedFraction();
                float fraction = 1 - circleFraction;
                mHookOffset = fraction * hookMaxValue;
                if (circleFraction >= circleMinFraction) {
                    mSweepAngle = (int) ((circleFraction - circleMinFraction) * circleMaxValue);
                } else {
                    mSweepAngle = 0;
                }
                mInnerCircleAlpha = (int) (fraction * 0xFF);
                invalidate();
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(mDuration).start();
    }

    private void startAnim() {
        clearAnimation();
        if (mChecked) {
            startCheckedAnim();
        } else {
            startUnCheckedAnim();
        }
    }


    private int getAlphaColor(int color, int alpha) {
        alpha = alpha < 0 ? 0 : alpha;
        alpha = alpha > 255 ? 255 : alpha;
        return (color & 0x00FFFFFF) | alpha << 24;
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    /**
     * setChecked with Animation
     *
     * @param checked true if checked, false if unchecked
     */
    @Override
    public void setChecked(boolean checked) {
        setChecked(checked, true);
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }

    /**
     * @param checked   true if checked, false if unchecked
     * @param animation true with animation,false without animation
     */
    public void setChecked(boolean checked, boolean animation) {
        if (checked == this.mChecked) {
            return;
        }
        setCheckedViewInner(checked, animation);
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onChange(this, mChecked);
        }
    }

    /**
     * @deprecated use {@link #setOnCheckedChangeListener(OnCheckedChangeListener)} instead
     */
    @Deprecated
    @Override
    public void setOnClickListener(OnClickListener l) {
        //Empty!
    }

    private void setCheckedViewInner(boolean checked, boolean animation) {
        this.mChecked = checked;
        if (animation) {
            startAnim();
        } else {
            if (mChecked) {
                mInnerCircleAlpha = 0xFF;
                mSweepAngle = 0;
                mHookOffset = mHookSize + mEndLeftHookOffset - mBaseLeftHookOffset;
            } else {
                mInnerCircleAlpha = 0x00;
                mSweepAngle = 360;
                mHookOffset = 0;
            }
            invalidate();
        }
    }

    private int dip(int dip) {
        return (int) getContext().getResources().getDisplayMetrics().density * dip;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    /**
     * setOnCheckedChangeListener
     *
     * @param listener the OnCheckedChangeListener listener
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.mOnCheckedChangeListener = listener;
    }

    public interface OnCheckedChangeListener {
        void onChange(UICheckBox view, boolean checked);
    }
}
