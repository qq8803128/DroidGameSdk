package droid.game.common.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import static droid.game.common.util.Utils.dp;

public class LoadingView extends View {
    private ValueAnimator valueAnimator;
    private Paint mPaint;
    private int mColor = Color.parseColor("#ffffff");
    private long mDuration = 1000;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LoadingView setLoadingColor(int color){
        mColor = color;
        return this;
    }

    public LoadingView setDuration(long duration){
        mDuration = duration;
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPaint == null){
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(dp(2));
        }

        int w = canvas.getWidth();
        int h = canvas.getHeight();
        if (isInEditMode()){
            w = dp(36);
            h = dp(36);
            mColor = 0xffcfcfcf;
        }

        int color = mColor;

        for (int i = 0; i < 12; i++){
            mPaint.setColor(Color.argb((int)(Color.alpha(color) * (1-i * 0.065f)),Color.red(color),Color.green(color),Color.blue(color)));
            canvas.drawLine(w / 2,dp(3),w / 2,dp(8),mPaint);
            canvas.rotate(30,w / 2, h / 2);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        try {
            if (valueAnimator == null) {
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
            valueAnimator.start();
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
}
