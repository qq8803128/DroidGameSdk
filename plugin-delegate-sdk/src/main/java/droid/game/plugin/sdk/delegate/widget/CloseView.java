package droid.game.plugin.sdk.delegate.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import static droid.game.common.util.UIDisplayHelper.dpToPx;

public class CloseView extends ImageView {
    private Paint mPaint;
    public CloseView(Context context) {
        super(context);
    }

    public CloseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CloseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPaint == null){
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(dpToPx(3));
            mPaint.setColor(0x95ff8200);
        }

        int w = canvas.getWidth();
        int h = canvas.getHeight();
        if (isInEditMode()){
            w = dpToPx(40);
            h = w;
        }
        int r = 12;
        canvas.drawLine(dpToPx(r),dpToPx(r),w - dpToPx(r), h - dpToPx(r),mPaint);
        canvas.drawLine(w - dpToPx(r),dpToPx(r),dpToPx(r),h - dpToPx(r),mPaint);
    }
}
