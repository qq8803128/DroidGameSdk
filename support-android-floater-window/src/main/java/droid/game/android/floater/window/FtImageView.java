package droid.game.android.floater.window;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import static droid.game.common.util.Utils.dp;

public class FtImageView extends ImageView {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int DIVIDER_COLOR = 0x75000000;
    private final int FILL_COLOR = 0xffafafaf;
    public FtImageView(Context context) {
        super(context);
    }

    public FtImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FtImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (getTranslationX() < -1.0f){
            drawLeft(canvas);
        }else if (getTranslationX() > 1.0f){
            drawRight(canvas);
        }else{
            drawNormal(canvas);
        }
    }

    private void drawRight(Canvas canvas) {
        int r = dp(4) / 2;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(r * 2);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(FILL_COLOR);

        int s = dp(10);
        RectF rectF = new RectF(s,s / 3,canvas.getWidth() - s,canvas.getHeight() - s / 3);
        canvas.drawArc(rectF,120,120,false,mPaint);

        int r1 = r;

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(r * 2 + 1);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(DIVIDER_COLOR);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        RectF rectF1 = new RectF(s  ,s / 3 ,canvas.getWidth() - s,canvas.getHeight() - s / 3);
        canvas.drawArc(rectF1,120,120,false,mPaint);
    }

    private void drawLeft(Canvas canvas) {
        int r = dp(4) / 2;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(r * 2);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(FILL_COLOR);

        int s = dp(10);
        RectF rectF = new RectF(s,s / 3,canvas.getWidth() - s,canvas.getHeight() - s / 3);
        canvas.drawArc(rectF,-60,120,false,mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(r * 2 + 1);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(DIVIDER_COLOR);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        RectF rectF1 = new RectF(s  - 1,s / 3 ,canvas.getWidth() - s,canvas.getHeight() - s / 3);
        canvas.drawArc(rectF1,-60,120,false,mPaint);
    }

    private void drawNormal(Canvas canvas) {
        int size = dp(10);

        mPaint.setXfermode(null);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0x95000000);
        canvas.drawOval(new RectF(0,0,canvas.getWidth(),canvas.getHeight()),mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp(2));
        mPaint.setColor(0xffcfcfcf);
        canvas.drawOval(new RectF(size,size,canvas.getWidth() - size,canvas.getHeight() - size),mPaint);
    }
}
