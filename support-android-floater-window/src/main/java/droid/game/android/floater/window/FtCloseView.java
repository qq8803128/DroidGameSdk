package droid.game.android.floater.window;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import droid.game.common.util.UIDisplayHelper;

import static droid.game.common.util.Utils.dp;

public class FtCloseView extends View {
    private Paint mPaint;
    private int mColor = Color.parseColor("#ffffff");
    public FtCloseView(Context context) {
        this(context,null);
    }

    public FtCloseView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FtCloseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPaint == null) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(UIDisplayHelper.dpToPx(1.5f));
            mPaint.setColor(mColor);
        }

        int w = canvas.getWidth();
        int h = canvas.getHeight();
        Path path;
        int s;

        //s = canvas.getWidth() / 4 - UIDisplayHelper.dpToPx(5);
        s = dp(7);

        path = new Path();
        path.moveTo(w / 2 - s,h / 2 - s);
        path.lineTo(w / 2 + s, h / 2 + s);
        canvas.drawPath(path,mPaint);

        path = new Path();
        path.moveTo(w / 2 + s,h / 2 - s);
        path.lineTo(w / 2 - s, h / 2 + s);
        canvas.drawPath(path,mPaint);
    }
}
