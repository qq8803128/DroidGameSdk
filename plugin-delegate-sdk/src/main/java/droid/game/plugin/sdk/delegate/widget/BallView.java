package droid.game.plugin.sdk.delegate.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import static droid.game.common.util.Utils.dp;


public class BallView extends LinearLayout {
    private ValueAnimator valueAnimator;
    public BallView(Context context) {
        this(context,null);
    }

    public BallView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);

        LayoutParams layoutParams;

        View view;

        view = new View(context);
        view.setBackgroundDrawable(getShowColor(0));
        layoutParams = new LayoutParams(dp(10),dp(10));
        layoutParams.leftMargin = dp(8);
        layoutParams.rightMargin = dp(8);
        addView(view,layoutParams);

        view = new View(context);
        view.setBackgroundDrawable(getShowColor(1));
        layoutParams = new LayoutParams(dp(10),dp(10));
        layoutParams.leftMargin = dp(8);
        layoutParams.rightMargin = dp(8);
        addView(view,layoutParams);

        view = new View(context);
        view.setBackgroundDrawable(getShowColor(2));
        layoutParams = new LayoutParams(dp(10),dp(10));
        layoutParams.leftMargin = dp(8);
        layoutParams.rightMargin = dp(8);
        addView(view,layoutParams);

        view = new View(context);
        view.setBackgroundDrawable(getShowColor(3));
        layoutParams = new LayoutParams(dp(10),dp(10));
        layoutParams.leftMargin = dp(8);
        layoutParams.rightMargin = dp(8);
        addView(view,layoutParams);

        view = new View(context);
        view.setBackgroundDrawable(getShowColor(4));
        layoutParams = new LayoutParams(dp(10),dp(10));
        layoutParams.leftMargin = dp(8);
        layoutParams.rightMargin = dp(8);
        addView(view,layoutParams);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try{
            if (valueAnimator == null){
                valueAnimator = ValueAnimator.ofFloat(0f,5f);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        int position = ((int) value) % 5;
                        float scale = value - (float) position;

                        scaleBall(position,scale);
                    }
                });
            }
            valueAnimator.setDuration(3000);
            valueAnimator.setRepeatCount(-1);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.start();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    private void scaleBall(int position,float scale){
        try {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                float s = position == i ? (scale + 1) : 1;
                if (s < 1f) s = 1f;
                if (s > 2f) s = 2f;
                child.setScaleX(s);
                child.setScaleY(s);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try{
            valueAnimator.cancel();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    private Drawable getShowColor(int i) {
        int[] colors = new int[]{
                0xffff3399,
                0xffcc6600,
                0xff33ffcc,
                0xff660066,
                0xff009933
        };
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(colors[i % 5]);
        return drawable;
    }

}