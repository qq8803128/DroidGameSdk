package droid.game.common.toast;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import droid.game.common.global.Global;

import static android.widget.ListPopupWindow.WRAP_CONTENT;
import static droid.game.common.util.Utils.dp;

public class ToastEx {
    private static Toast lastToast = null;
    private static boolean allowQueue = true;

    public static void shortShow(String message) {
        try {
            if (TextUtils.isEmpty(message)) {
                message = "unknown";
                return;
            }
            show(Global.getApplication(), message, Toast.LENGTH_SHORT);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void longShow(String message) {
        try {
            if (TextUtils.isEmpty(message)) {
                message = "unknown";
                return;
            }
            show(Global.getApplication(), message, Toast.LENGTH_LONG);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void show(Context context, String message, int duration) {
        final Toast currentToast = Toast.makeText(context, "", duration);
        final LinearLayout toastLayout = new ToastBackground(context);

        final TextView toastTextView = new TextView(context);
        toastTextView.setPadding(dp(18), dp(4), dp(18), dp(4));
        toastTextView.setGravity(Gravity.CENTER);
        toastTextView.setMinHeight(dp(44));
        toastTextView.setMaxLines(3);
        toastLayout.addView(toastTextView, WRAP_CONTENT, WRAP_CONTENT);

        toastTextView.setText(message);
        toastTextView.setTextColor(0xffffffff);
        try {
            toastTextView.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
        toastTextView.setTextSize(15);

        currentToast.setView(toastLayout);

        if (!allowQueue) {
            if (lastToast != null) {
                lastToast.cancel();
            }
            lastToast = currentToast;
        }
        currentToast.show();
    }

    private static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    static class ToastBackground extends LinearLayout{
        private Paint mPaint,mStrokePaint;
        public ToastBackground(Context context) {
            this(context,null);
        }

        public ToastBackground(Context context, AttributeSet attrs) {
            this(context, attrs,0);
        }

        public ToastBackground(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            setWillNotDraw(false);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (mPaint == null){
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(0xCF5060BA);

                mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mStrokePaint.setStyle(Paint.Style.STROKE);
                mStrokePaint.setStrokeWidth(1);
                mStrokePaint.setColor(0x2f010101);
            }

            canvas.drawRoundRect(new RectF(0,0,canvas.getWidth(),canvas.getHeight()),canvas.getHeight() / 2,canvas.getHeight() /2 ,mPaint);
            canvas.drawRoundRect(new RectF(0,0,canvas.getWidth(),canvas.getHeight()),canvas.getHeight() / 2,canvas.getHeight() /2 ,mStrokePaint);
        }
    }
}
