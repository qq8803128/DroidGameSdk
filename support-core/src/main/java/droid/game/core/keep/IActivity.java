package droid.game.core.keep;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;

public interface IActivity {
    void onCreate(Activity activity);

    void onResume(Activity activity);

    void onRestart(Activity activity);

    void onStart(Activity activity);

    void onPause(Activity activity);

    void onStop(Activity activity);

    void onDestroy(Activity activity);

    void onNewIntent(Activity activity, Intent intent);

    void onConfigurationChanged(Activity activity, Configuration newConfig);

    void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data);

    void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults);
}
