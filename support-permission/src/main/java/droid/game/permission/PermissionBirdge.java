package droid.game.permission;

import android.app.Activity;
import android.os.Build;
import droid.game.common.bus.BusProvider;
import droid.game.common.keep.Consumer;
import droid.game.common.permission.IPermission;
import droid.game.common.permission.PermissionManager;
import droid.game.permission.util.PermissionUtils;

import java.io.File;
import java.util.List;

public class PermissionBirdge implements IPermission {
    public static void setup(){
        PermissionManager.setup(new PermissionBirdge());
    }

    private Consumer<Boolean> mRuntime,mInstall;
    PermissionBirdge() {
        super();
        BusProvider.get().register(this);
    }

    @Override
    public void runtime(Activity activity, Consumer<Boolean> consumer, List<String> permissions) {
        if (PermissionUtils.getTargetSdkVersion(activity) < Build.VERSION_CODES.M || Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            consumer.accept(true);
        }else{
            //mRuntime = consumer;
            //PermissionActivity.requestPermission(activity,PermissionUtils.array(permissions));
            consumer.accept(true);
        }
    }

    @Override
    public void install(Activity activity, Consumer<Boolean> consumer, File file) {
        mInstall = consumer;
        PermissionActivity.requestInstall(activity,file);
    }

    public static class Result{
        public Result(int type,boolean success) {
            super();
        }
    }
}
