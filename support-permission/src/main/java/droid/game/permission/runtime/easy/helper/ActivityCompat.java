package droid.game.permission.runtime.easy.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import droid.game.annotation.IntRange;
import droid.game.annotation.NonNull;

public class ActivityCompat {
    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermissions(final @NonNull Activity activity,
                                          final @NonNull String[] permissions, final @IntRange(from = 0) int requestCode) {
        activity.requestPermissions(permissions, requestCode);
    }

    public static boolean shouldShowRequestPermissionRationale(@NonNull Activity activity,
                                                               @NonNull String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            return activity.shouldShowRequestPermissionRationale(permission);
        }
        return false;
    }
}
