package droid.game.common.permission;

import android.app.Activity;
import droid.game.common.keep.Consumer;

import java.io.File;
import java.util.List;

public interface IPermission {
    void runtime(Activity activity, Consumer<Boolean> consumer, List<String> permissions);
    void install(Activity activity, Consumer<Boolean> consumer, File file);
}
