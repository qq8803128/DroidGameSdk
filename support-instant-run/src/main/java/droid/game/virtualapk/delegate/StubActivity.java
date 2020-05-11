package droid.game.virtualapk.delegate;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

/**
 * Created by qiaopu on 2018/6/13.
 */
public class StubActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        // Go to the main activity
        Intent mainIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        
        if (mainIntent == null) {
            mainIntent = new Intent(Intent.ACTION_MAIN);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            mainIntent.setPackage(getPackageName());
    
            ResolveInfo resolveInfo = getPackageManager().resolveActivity(mainIntent, 0);
    
            if (resolveInfo != null) {
                mainIntent.setClassName(this, resolveInfo.activityInfo.name);
            }
        }
    
        startActivity(mainIntent);
        
        finish();
    }
}
