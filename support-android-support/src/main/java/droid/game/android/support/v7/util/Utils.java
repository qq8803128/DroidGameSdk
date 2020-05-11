package droid.game.android.support.v7.util;

import android.content.res.Resources;

public class Utils {
    public static int dpToPx(float dpValue){
        return (int)(dpValue * Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }
}
