package droid.game.permission;

import android.app.Activity;
import android.os.Build;
import droid.game.common.bus.BusProvider;
import droid.game.common.keep.Consumer;
import droid.game.common.permission.IPermission;
import droid.game.common.permission.PermissionManager;
import droid.game.open.source.otto.Subscribe;
import droid.game.permission.util.PermissionUtils;

import java.io.File;
import java.util.List;

public class PermissionBirdge implements IPermission {
    public static void setup() {
        PermissionManager.setup(permissionManager());
    }

    private static IPermission permissionManager() {
        return Holder.holder;
    }

    private static class Holder {
        private final static IPermission holder = new PermissionBirdge();
    }

    private Consumer<Boolean> mRuntime, mInstall;

    PermissionBirdge() {
        super();
        BusProvider.get().register(this);
    }

    @Override
    public void runtime(Activity activity, Consumer<Boolean> consumer, List<String> permissions) {
        if (PermissionUtils.getTargetSdkVersion(activity) < Build.VERSION_CODES.M || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            consumer.accept(true);
        } else {
            mRuntime = consumer;
            PermissionActivity.requestPermission(activity, PermissionUtils.array(permissions));
        }
    }

    @Override
    public void install(Activity activity, Consumer<Boolean> consumer, File file) {
        mInstall = consumer;
        PermissionActivity.requestInstall(activity, file);
    }

    @Subscribe
    public void onPermissionResult(Result result) {
        if (result != null) {
            switch (result.mType) {
                case Result.INSTALL:
                    mRuntime.accept(result.isSuccess());
                    break;
                case Result.RUNTIME:
                    break;
            }
        }
    }

    public static class Result {
        public static final int RUNTIME = 0;
        public static final int INSTALL = 1;

        private int mType;
        private boolean mSuccess;

        public Result(int type, boolean success) {
            super();
            mType = type;
            mSuccess = success;
        }

        public int getType() {
            return mType;
        }

        public boolean isSuccess() {
            return mSuccess;
        }
    }
}
