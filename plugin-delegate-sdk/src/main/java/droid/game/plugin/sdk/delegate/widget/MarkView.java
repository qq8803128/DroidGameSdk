package droid.game.plugin.sdk.delegate.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import static droid.game.common.util.UIDisplayHelper.dpToPx;

public class MarkView extends View {
    private Paint mPaint;
    public MarkView(Context context) {
        super(context);
    }

    public MarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        if (isInEditMode()){
            w = 1080;
            h = dpToPx(48);
        }
        Path path = new Path();
        path.moveTo(0,0);
        path.quadTo( w / 4 ,h / 10 * 8, w / 2, h / 10 * 8);
        path.quadTo(w / 4 * 3,h /  10 * 8, w , 0);
        path.lineTo(w,h);
        path.lineTo(0,h);
        path.close();
        if (mPaint == null){
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.WHITE);
        }

        canvas.drawPath(path,mPaint);
    }
}
