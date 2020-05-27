package droid.game.permission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import droid.game.common.bus.BusProvider;
import droid.game.common.global.Global;
import droid.game.common.json.Json;
import droid.game.common.json.JsonParser;
import droid.game.permission.runtime.PermissionScaner;
import droid.game.permission.runtime.easy.EasyPermissions;
import droid.game.permission.util.PermissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionActivity extends Activity {
    private static PermissionInfo sPermissionInfo;
    private static final String KEY_TYPE = "KEY_TYPE";
    private static final String KEY_PERMISSIONS = "KEY_PERMISSIONS";

    public static final int TYPE_PERMISSION = 1;
    public static final int TYPE_INSTALL = 2;

    public static void requestPermission(Activity activity,  String[] permissions) {
        initPermissionInfoJson(activity);
        if (sPermissionInfo.isAutoRequestPermission()) {
            ArrayList<String> dangerousPermissions = (ArrayList<String>) Arrays.asList(permissions);
            if (permissions == null || permissions.length == 0) {
                dangerousPermissions = (ArrayList<String>) PermissionScaner.sacnPermissions(activity, sPermissionInfo.getRemovePermissions());
            }
            if (dangerousPermissions != null || dangerousPermissions.size() >= 0){
                Intent intent = new Intent(activity,PermissionActivity.class);
                intent.putStringArrayListExtra(KEY_PERMISSIONS,dangerousPermissions);
                intent.putExtra(KEY_TYPE,TYPE_PERMISSION);

                activity.startActivity(intent);
                return;
            }else{
                BusProvider.get().post(new PermissionBirdge.Result(PermissionBirdge.Result.RUNTIME,true));
            }
        }

        BusProvider.get().post(new PermissionBirdge.Result(TYPE_INSTALL,true));
    }

    public static void requestInstall(Activity activity, File file) {

    }

    private static void initPermissionInfoJson(Context context){
        if (sPermissionInfo == null){
            try {
                sPermissionInfo = JsonParser.parser(
                        PermissionUtils.open(context,"android.gamecenter.system/permissions.json"),
                        PermissionInfo.class
                );
            } catch (Exception e) {
                e.printStackTrace();
                sPermissionInfo = new PermissionInfo();
            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int operation = intent.getIntExtra(KEY_TYPE, -1);

        switch (operation) {
            case TYPE_PERMISSION: {
                String[] permissions = intent.getStringArrayExtra(KEY_PERMISSIONS);
                requestPermissions(permissions, TYPE_PERMISSION);
                break;
            }
            case TYPE_INSTALL: {
                //Intent manageIntent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                //manageIntent.setData(Uri.fromParts("package", getPackageName(), null));
                //startActivityForResult(manageIntent, TYPE_INSTALL);
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
    }

    private static class PermissionInfo extends Json {
        public String getUserProtocolUrl(){
            return get("","user-protocol");
        }

        public boolean isAutoRequestPermission(){
            boolean json = get(true,"auto-request-permission");
            boolean target = PermissionUtils.getTargetSdkVersion(Global.getApplication()) >= Build.VERSION_CODES.M;
            boolean android = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
            return json && target && android;
        }

        public List<String> getRemovePermissions(){
            return get(new ArrayList<String>(),"remove-permission");
        }
    }
}
