package droid.game.common.suspen;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import droid.game.common.widget.LoadingView;
import droid.game.common.widget.RoundProgressBar;

import static droid.game.common.util.Utils.dp;

public class SuspenNight implements Theme {
    public static Theme theme() {
        return Holder.holder;
    }

    private final int COLOR_BACKGROUND = Color.parseColor("#ef000000");
    private final int COLOR_STROKEN = Color.parseColor("#15f1f2f3");

    private final int TITLE_TEXT_COLOR = Color.parseColor("#ff4081");
    private final float TITLE_TEXT_SIZE = 18f;
    private final Typeface TITLE_TYPEFACE = Typeface.DEFAULT_BOLD;
    private final int TITLE_GRAVITY = Gravity.LEFT;

    private final int CONTENT_TEXT_COLOR = Color.parseColor("#a1a2a3");
    private final float CONTENT_TEXT_SIZE = 14.0f;
    private final Typeface CONTENT_TYPEFACE = Typeface.DEFAULT;
    private final int CONTENT_GRAVITY = Gravity.LEFT;

    private final int BUTTON_BACKGROUND_COLOR1 = Color.parseColor("#15666666");
    private final int BUTTON_BACKGROUND_COLOR = Color.parseColor("#15ffffff");
    private final int BUTTON_TEXT_COLOR = Color.parseColor("#ff4081");

    SuspenNight() {
        super();
    }

    @Override
    public void setContainerStyle(View view) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(dp(8));
        drawable.setColor(COLOR_BACKGROUND);
        view.setBackgroundDrawable(drawable);

        drawable.setStroke(1, COLOR_STROKEN);
    }

    @Override
    public void setTitleViewStyle(TextView textView) {
        textView.setTextColor(TITLE_TEXT_COLOR);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TITLE_TEXT_SIZE);
        textView.setTypeface(TITLE_TYPEFACE);
        textView.setGravity(TITLE_GRAVITY);
    }

    @Override
    public void setContentViewStyle(TextView textView) {
        textView.setTextColor(CONTENT_TEXT_COLOR);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, CONTENT_TEXT_SIZE);
        textView.setTypeface(CONTENT_TYPEFACE);
        textView.setGravity(CONTENT_GRAVITY);
        textView.setPadding(0, dp(2), 0, 0);
    }

    @Override
    public void setButtonViewStyle(View topLine, View centerLine, TextView positive, TextView negative) {
        if (positive != null) {
            positive.setPadding(dp(14), 0, dp(14), 0);
            positive.setTextSize(13);
            positive.setGravity(Gravity.CENTER);

            GradientDrawable pressed = new GradientDrawable();
            pressed.setCornerRadius(dp(14));
            pressed.setColor(BUTTON_BACKGROUND_COLOR1);

            GradientDrawable normal = new GradientDrawable();
            normal.setCornerRadius(dp(14));
            normal.setColor(BUTTON_BACKGROUND_COLOR);

            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressed);
            stateListDrawable.addState(new int[]{}, normal);

            positive.setTextColor(BUTTON_TEXT_COLOR);

            positive.setBackgroundDrawable(stateListDrawable);
            positive.setClickable(true);

        }
    }

    @Override
    public void setLoadingStyle(LoadingView loadingView) {

    }

    @Override
    public void setProgressBarStyle(ProgressBar progressBar, TextView textView) {

    }

    @Override
    public void setRoundProgressBarStyle(RoundProgressBar progressBar) {

    }


    private static class Holder {
        private static final Theme holder = new SuspenNight();
    }
}
