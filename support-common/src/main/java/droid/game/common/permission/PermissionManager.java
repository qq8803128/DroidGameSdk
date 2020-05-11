package droid.game.common.permission;

import android.app.Activity;
import droid.game.common.keep.Consumer;

import java.io.File;
import java.util.List;

public class PermissionManager {
    private static IPermission sPermission;
    public static void setup(IPermission permission){
        sPermission = permission;
    }

    public static void runtime(Activity activity, Consumer<Boolean> consumer, List<String> permissions){
        if (sPermission == null){
            consumer.accept(true);
        }else{
            sPermission.runtime(activity,consumer,permissions);
        }
    }

    public static void install(Activity activity, Consumer<Boolean> consumer,File file){
        if (sPermission == null){
            consumer.accept(false);
        }else{
            sPermission.install(activity,consumer,file);
        }
    }
}
