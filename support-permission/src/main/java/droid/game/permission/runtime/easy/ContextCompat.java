package droid.game.permission.runtime.easy;

import android.content.Context;
import android.os.Process;
import droid.game.annotation.NonNull;

class ContextCompat {
    public static int checkSelfPermission(@NonNull Context context, @NonNull String permission) {
        if (permission == null) {
            throw new IllegalArgumentException("permission is null");
        }

        return context.checkPermission(permission, android.os.Process.myPid(), Process.myUid());
    }
}
