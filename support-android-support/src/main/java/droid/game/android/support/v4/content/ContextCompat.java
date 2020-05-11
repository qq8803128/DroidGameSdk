package droid.game.android.support.v4.content;

import android.content.Context;
import android.os.Build;
import droid.game.annotation.NonNull;

public class ContextCompat {
    public static int getColor(@NonNull Context context,int id) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getColor(id);
        } else {
            return context.getResources().getColor(id);
        }
    }

}
