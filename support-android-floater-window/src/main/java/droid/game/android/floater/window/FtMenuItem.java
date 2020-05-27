package droid.game.android.floater.window;

import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static droid.game.common.util.Utils.dp;

public class FtMenuItem extends LinearLayout {
    public FtMenuItem(Context context) {
        super(context);
    }

    public FtMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FtMenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMenuItem(String text, Drawable drawable) {
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);

        Context ctx = getContext();
        ImageView imageView = new ImageView(ctx);
        addView(imageView,dp(15),dp(15));
        imageView.setImageDrawable(drawable);

        TextView textView = new TextView(ctx);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(0xffffffff);
        textView.setTextSize(7f);
        textView.setMaxLines(1);
        textView.setPadding(dp(2),dp(2),dp(2),0);
        textView.setText(text);
        addView(textView,MATCH_PARENT,WRAP_CONTENT);
        imageView.setColorFilter(getColorFilter());

    }

    private ColorMatrixColorFilter getColorFilter(){
        float[] filter = new float[]{
                1,0,0,0,1,
                1,0,0,0,1,
                1,0,0,0,1,
                1,0,0,0,1
        };
        return new ColorMatrixColorFilter(filter);
    }
}
