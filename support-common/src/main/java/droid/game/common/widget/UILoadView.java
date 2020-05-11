package droid.game.common.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import droid.game.common.util.UIDisplayHelper;

public class UILoadView extends View {
    public static final int NONE = -1;
    public static final int LOADING = 0;
    public static final int SUCCESS = 1;
    public static final int ERROR = 2;
    public static final int INFO = 3;

    private ValueAnimator valueAnimator;
    private Paint mPaint;
    private int mColor = Color.parseColor("#333333");
    private long mDuration = 2000;
    private int mTheme = NONE;

    public UILoadView(Context context) {
        super(context);
    }

    public UILoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UILoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UILoadView setLoadingColor(int color){
        mColor = color;
        return this;
    }

    public UILoadView setDuration(long duration){
        mDuration = duration;
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (LOADING == mTheme) {
            if (mPaint == null) {
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setStrokeCap(Paint.Cap.ROUND);
                mPaint.setStrokeWidth(UIDisplayHelper.dpToPx(2));
            }

            int color = mColor;

            for (int i = 0; i < 12; i++) {
                mPaint.setColor(Color.argb((int) (Color.alpha(color) * (1 - i * 0.065f)), Color.red(color), Color.green(color), Color.blue(color)));
                canvas.drawLine(canvas.getWidth() / 2, UIDisplayHelper.dpToPx(3), canvas.getWidth() / 2, UIDisplayHelper.dpToPx(8), mPaint);
                canvas.rotate(30, canvas.getWidth() / 2, canvas.getHeight() / 2);
            }
        }else{
            if (mPaint == null) {
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(UIDisplayHelper.dpToPx(1.5f));
                mPaint.setColor(mColor);
            }

            drawSymble(canvas);

            canvas.drawCircle(canvas.getWidth() / 2,canvas.getHeight() / 2,canvas.getHeight() / 2 - UIDisplayHelper.dpToPx(1f),mPaint);
        }
    }

    private void drawSymble(Canvas canvas) {
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        Path path;
        int s;
        switch (mTheme){
            case SUCCESS:
                s = canvas.getWidth() / 4 ;
                path = new Path();
                path.moveTo(w / 2 - s,h / 2);
                path.lineTo(w / 2 -s / 4, h / 2 + s);
                path.lineTo(w / 2 + s, h / 2 -s );
                canvas.drawPath(path,mPaint);
                break;
            case ERROR:
                s = canvas.getWidth() / 4 - UIDisplayHelper.dpToPx(1);

                path = new Path();
                path.moveTo(w / 2 - s,h / 2 - s);
                path.lineTo(w / 2 + s, h / 2 + s);
                canvas.drawPath(path,mPaint);

                path = new Path();
                path.moveTo(w / 2 + s,h / 2 - s);
                path.lineTo(w / 2 - s, h / 2 + s);
                canvas.drawPath(path,mPaint);
                break;
            case INFO:
                s = canvas.getWidth() / 4 + UIDisplayHelper.dpToPx(1);
                path = new Path();
                path.moveTo(w / 2,h / 2 - s);
                path.lineTo(w / 2, h / 2 - s + UIDisplayHelper.dpToPx(2));
                canvas.drawPath(path,mPaint);

                path = new Path();
                path.moveTo(w / 2, h / 2 - s + UIDisplayHelper.dpToPx(5f));
                path.lineTo(w / 2, h / 2 + s);
                canvas.drawPath(path,mPaint);
                break;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        try {
            if (valueAnimator == null && mDuration > 0 && mTheme == LOADING) {
                valueAnimator = ValueAnimator.ofFloat(0, 360);
                valueAnimator.setDuration(mDuration);
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.setRepeatCount(-1);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        try {
                            float roatation = (float) valueAnimator.getAnimatedValue();
                            roatation = roatation / 30;
                            setRotation(((int) roatation) * 30);

                            //Log.e("TAG",roatation + "");
                        }catch (Throwable e){
                            e.printStackTrace();
                        }
                    }
                });
            }
            if (valueAnimator != null){
                valueAnimator.start();
            }
        }catch (Throwable e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
        }catch (Throwable e){
            e.printStackTrace();
        }

    }

    public void setTheme(int theme) {
        mTheme = theme;
    }
}
