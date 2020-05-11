package droid.game.common.global;

import android.app.Activity;
import android.app.Application;

public class Global {
    private static Application sApplication;
    private static Activity sActivity;

    public static Application getApplication() {
        return sApplication;
    }

    public static void setApplication(Application application) {
        Global.sApplication = application;
    }

    public static Activity getActivity() {
        return sActivity;
    }

    public static void setActivity(Activity activity) {
        Global.sActivity = activity;
    }
}
