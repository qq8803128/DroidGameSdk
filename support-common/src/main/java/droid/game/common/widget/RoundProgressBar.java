package droid.game.common.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class RoundProgressBar extends View {
    /**
     * arcPaint
     */
    private Paint arcPaint;
    /**
     * arcRect
     */
    private RectF arcRect;
    /**
     * textPaint
     */
    private Paint textPaint;
    /**
     * arc StrokeWidth
     */
    private int strokeWidth;
    /**
     * countDown Arc StrokeColor
     */
    private int strokeColor;
    /**
     * progress
     */
    private int progress;
    /**
     * countDown millis default is 3000ms
     */
    private int countDownTimeMillis;
    /**
     * center background paint
     */
    private Paint centerBgPaint;
    /**
     * center background
     */
    private int centerBackground;
    /**
     * center text
     */
    private String centerText;
    /**
     * placeHolder if there is none text
     */
    private String emptyText = "100%";
    /**
     * center textColor
     */
    private int centerTextColor;
    /**
     * center textSize
     */
    private float centerTextSize;
    /**
     * measure text bounds
     */
    private Rect textBounds;
    /**
     * arc start angle default is -90
     */
    private int startAngle;
    /**
     * if is auto start,default is true
     */
    private boolean isAutoStart;
    /**
     * progress change listener
     */
    private ProgressChangeListener mProgressChangeListener;
    /**
     * arc sweep direction default is forward
     */
    private Direction mDirection;
    /**
     * direction index
     */
    private int directionIndex;
    private final Direction[] mDirections = {
            Direction.FORWARD,
            Direction.REVERSE
    };

    public enum Direction {
        /**
         * forward
         */
        FORWARD(0),
        /**
         * reverse
         */
        REVERSE(1);

        Direction(int ni) {
            nativeInt = ni;
        }

        final int nativeInt;
    }

    /**
     * value animator
     */
    private ValueAnimator animator;
    /**
     * if true draw outsideWrapper false otherwise
     */
    private boolean shouldDrawOutsideWrapper;
    /**
     * outsideWrapper color
     */
    private int outsideWrapperColor;
    /**
     * default space between view to bound
     */
    private int defaultSpace;
    /**
     * isSupport end to start, if true progress = progress - 360.
     */
    private boolean isSupportEts;

    private long currentTime;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        strokeWidth = (int) dp2px(2);
        strokeColor = Color.BLACK;
        startAngle =  -90;
        centerText = "";
        centerTextSize = sp2px(12);
        centerTextColor = Color.WHITE;
        centerBackground =Color.parseColor("#808080");
        countDownTimeMillis = 3 * 1000;
        directionIndex =  0;
        isAutoStart = false;
        shouldDrawOutsideWrapper =  false;
        outsideWrapperColor = Color.parseColor("#E8E8E8");
        isSupportEts = false;
        defaultSpace = strokeWidth * 2;
        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(centerTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        centerBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        centerBgPaint.setStyle(Paint.Style.FILL);

        arcRect = new RectF();
        textBounds = new Rect();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int minWidth = getMinWidth(widthMode, widthSize);
        int minHeight = getMinHeight(heightMode, heightSize);
        if (minWidth != minHeight) {
            int suggestedSize = Math.max(minWidth, minHeight);
            minWidth = suggestedSize;
            minHeight = suggestedSize;
        }
        setMeasuredDimension(minWidth, minHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        arcRect.left = defaultSpace >> 1;
        arcRect.top = defaultSpace >> 1;
        arcRect.right = w - (defaultSpace >> 1);
        arcRect.bottom = h - (defaultSpace >> 1);
    }

    /**
     * getMinWidth textHeight + paddingLeft + paddingRight + arcStrokeWidth * 2
     *
     * @param mode         mode
     * @param measuredSize measuredSize
     * @return minWidth
     */
    private int getMinWidth(int mode, int measuredSize) {
        int suggestSize = 0;
        switch (mode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                if (TextUtils.isEmpty(centerText)) {
                    textPaint.getTextBounds(emptyText, 0, emptyText.length(), textBounds);
                } else {
                    textPaint.getTextBounds(centerText, 0, centerText.length(), textBounds);
                }
                suggestSize = getPaddingLeft() + getPaddingRight() + textBounds.width() + defaultSpace;
                break;
            case MeasureSpec.EXACTLY:
                suggestSize = measuredSize;
                break;
            default:
        }
        return suggestSize;
    }

    /**
     * getMinHeight similar to {@link #getMinWidth(int, int)}.
     *
     * @param mode         mode
     * @param measuredSize measuredSize
     * @return minHeight
     */
    private int getMinHeight(int mode, int measuredSize) {
        int suggestSize = 0;
        switch (mode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                if (TextUtils.isEmpty(centerText)) {
                    textPaint.getTextBounds(emptyText, 0, emptyText.length(), textBounds);
                } else {
                    textPaint.getTextBounds(centerText, 0, centerText.length(), textBounds);
                }
                suggestSize = getPaddingTop() + getPaddingBottom() + textBounds.height() + defaultSpace;
                break;
            case MeasureSpec.EXACTLY:
                suggestSize = measuredSize;
                break;
            default:
        }
        return suggestSize;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawCenterBackground(canvas);
        if (shouldDrawOutsideWrapper) {
            drawOutsideWrapper(canvas);
        }
        drawArc(canvas);
        drawCenterText(canvas);
    }

    /**
     * draw center background circle
     *
     * @param canvas
     */
    private void drawCenterBackground(Canvas canvas) {
        centerBgPaint.setColor(centerBackground);
        canvas.drawCircle(arcRect.centerX(), arcRect.centerY(), (arcRect.width() - (defaultSpace >> 2)) / 2, centerBgPaint);
    }

    /**
     * draw outside arc wrapper if needed
     *
     * @param canvas
     */
    private void drawOutsideWrapper(Canvas canvas) {
        arcPaint.setColor(outsideWrapperColor);
        canvas.drawArc(arcRect, 0, 360, false, arcPaint);
    }

    /**
     * draw sweep arc
     * core
     *
     * @param canvas
     */
    private void drawArc(Canvas canvas) {
        arcPaint.setStrokeWidth(strokeWidth);
        arcPaint.setColor(strokeColor);
        canvas.drawArc(arcRect, startAngle, isSupportEts ? progress - 360 : progress, false, arcPaint);
    }

    /**
     * draw centerText
     *
     * @param canvas
     */
    private void drawCenterText(Canvas canvas) {
        textPaint.setColor(centerTextColor);
        if (TextUtils.isEmpty(centerText)) {
            canvas.drawText(Math.abs((int) (progress / 3.6)) + "%", arcRect.centerX(), arcRect.centerY() - (textPaint.descent() + textPaint.ascent()) / 2, textPaint);
        } else {
            canvas.drawText(centerText, arcRect.centerX(), arcRect.centerY() - (textPaint.descent() + textPaint.ascent()) / 2, textPaint);
        }
    }

    /**
     * start
     */
    public void start() {
        initAnimator(countDownTimeMillis, mDirection);
    }

    /**
     * stop
     */
    public void stop() {
        if (animator != null) {
            animator.cancel();
        }
    }

    /**
     * pause
     */
    public void pause() {
        if (animator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                animator.pause();
            } else {
                currentTime = animator.getCurrentPlayTime();
                animator.cancel();
            }
        }
    }

    /**
     * resume
     */
    public void resume() {
        if (animator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                animator.resume();
            } else {
                animator.setCurrentPlayTime(currentTime);
                animator.start();
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setDirection(mDirections[directionIndex]);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    /**
     * init Animator
     *
     * @param duration  duration
     * @param direction sweep direction
     */
    private void initAnimator(int duration, Direction direction) {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        int start = 0;
        int end = 360;
        if (direction == Direction.REVERSE) {
            start = 360;
            end = 0;
        }
        animator = ValueAnimator.ofInt(start, end).setDuration(duration);
        animator.setRepeatCount(0);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (int) animation.getAnimatedValue();
                if (mProgressChangeListener != null) {
                    mProgressChangeListener.onProgressChanged((int) (progress / 3.6));
                }
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mProgressChangeListener != null) {
                    mProgressChangeListener.onFinish();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }

    /**
     * set sweep arc strokeWidth
     *
     * @param strokeWidth strokeWidth
     */
    public void setStrokeWidth(int strokeWidth) {
        if (strokeWidth > 0) {
            this.strokeWidth = strokeWidth;
        }
    }

    /**
     * set sweep arc strokeColor
     *
     * @param strokeColor strokeColor
     */
    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    /**
     * set countDown millis
     *
     * @param countDownTimeMillis countDownTimeMillis
     */
    public void setCountDownTimeMillis(int countDownTimeMillis) {
        this.countDownTimeMillis = countDownTimeMillis;
    }

    /**
     * set center background (color)
     *
     * @param centerBackground centerBackground
     */
    public void setCenterBackground(int centerBackground) {
        this.centerBackground = centerBackground;
    }

    /**
     * set center text
     * if is none , then start progress text countDown(100% ~ 0% | 0 % ~ 100%)
     *
     * @param centerText centerText
     */
    public void setCenterText(String centerText) {
        this.centerText = centerText;
    }

    /**
     * set center textColor
     *
     * @param centerTextColor centerTextColor
     */
    public void setCenterTextColor(int centerTextColor) {
        this.centerTextColor = centerTextColor;
    }

    /**
     * set center textSize
     *
     * @param centerTextSize centerTextSize
     */
    public void setCenterTextSize(float centerTextSize) {
        this.centerTextSize = centerTextSize;
    }

    /**
     * set sweep start angle
     *
     * @param startAngle start angle default is -90
     */
    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    /**
     * set if is auto start
     *
     * @param isAutoStart if is auto start
     */
    public void setAutoStart(boolean isAutoStart) {
        this.isAutoStart = isAutoStart;
    }

    /**
     * set progressChange listener
     *
     * @param progressChangeListener progressChangeListener
     */
    public void setProgressChangeListener(ProgressChangeListener progressChangeListener) {
        mProgressChangeListener = progressChangeListener;
    }

    /**
     * set direction
     *
     * @param direction sweep direction
     */
    public void setDirection(Direction direction) {
        if (direction == null) {
            throw new RuntimeException("Direction is null");
        }
        mDirection = direction;
        switch (direction) {
            default:
            case FORWARD:
                progress = 0;
                break;
            case REVERSE:
                progress = 360;
                break;
        }
        if (isAutoStart) {
            start();
        }
    }

    /**
     * set progress by your self
     *
     * @param progress progress 0-360
     */
    public void setProgress(int progress) {
        if (progress > 360) {
            progress = 360;
        } else if (progress < 0) {
            progress = 0;
        }
        this.progress = progress;
        invalidate();
    }

    /**
     * set progress percent
     *
     * @param progressPercent 0-100
     */
    public void setProgressPercent(int progressPercent) {
        if (progressPercent > 100) {
            progressPercent = 100;
        } else if (progressPercent < 0) {
            progressPercent = 0;
        }
        this.progress = (int) (progressPercent * 3.6);
        invalidate();
    }

    public void setShouldDrawOutsideWrapper(boolean shouldDrawOutsideWrapper) {
        this.shouldDrawOutsideWrapper = shouldDrawOutsideWrapper;
    }

    public void setOutsideWrapperColor(int outsideWrapperColor) {
        this.outsideWrapperColor = outsideWrapperColor;
    }

    public boolean isSupportEts() {
        return isSupportEts;
    }

    public void setSupportEts(boolean supportEts) {
        isSupportEts = supportEts;
    }

    private float sp2px(float inParam) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, inParam, getContext().getResources().getDisplayMetrics());
    }

    private float dp2px(float dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return dp < 0 ? dp : Math.round(dp * displayMetrics.density);
    }

    public interface ProgressChangeListener {
        /**
         * onFinish
         */
        void onFinish();

        /**
         * onProgressChanged
         *
         * @param progress
         */
        void onProgressChanged(int progress);
    }


}