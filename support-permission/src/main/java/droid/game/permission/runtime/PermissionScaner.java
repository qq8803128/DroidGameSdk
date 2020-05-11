package droid.game.permission.runtime;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static android.Manifest.permission.*;

public class PermissionScaner {

    private static Map<String,String> sGroupMap = new HashMap<>();

    public static List<String> sacnPermissions(Context context,List<String> removes){
        List<String> permissions = new ArrayList<>();

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pkgInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            String sharedPkgList[] = pkgInfo.requestedPermissions;

            for (String permission : sharedPkgList){
                PermissionInfo tmpPermInfo = null;
                try {
                    tmpPermInfo = pm.getPermissionInfo(permission, 0);
                }catch (Throwable e){
                    e.printStackTrace();
                }

                if (tmpPermInfo != null && tmpPermInfo.protectionLevel ==  PermissionInfo.PROTECTION_DANGEROUS){
                    if (!removes.contains(permission) && checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED) {
                        permissions.add(permission);
                    }
                }
            }

            Log.e("dangerous-permission",permissions.toString());
        }catch (Throwable e){
            e.printStackTrace();
        }
        return permissions;
    }

    public static int checkSelfPermission( Context context, String permission){
        if (permission == null) {
            throw new IllegalArgumentException("permission is null");
        }

        return context.checkPermission(permission, android.os.Process.myPid(), Process.myUid());
    }

    public static List<String> getDangerousPermissionsGroup(Context context,List<String> permissions){
        checkGroup();
        List<String> groups = new ArrayList<>();
        if (permissions == null || permissions.size() <= 0){
            return groups;
        }
        for (String permission : permissions){
            /*
            PackageManager pm = context.getPackageManager();
            PermissionInfo tmpPermInfo = null;
            try {
                tmpPermInfo = pm.getPermissionInfo(permission, 0);
            }catch (Throwable e){
                e.printStackTrace();
            }
            if (tmpPermInfo != null){
                String group = tmpPermInfo.group;
                if (!groups.contains(group)){
                    groups.add(group);
                }
            }
            */
            String tmpPermInfo = sGroupMap.get(permission);
            if (!TextUtils.isEmpty(tmpPermInfo)){
                String group = tmpPermInfo;
                if (!groups.contains(group)){
                    groups.add(group);
                }
            }
        }
        return groups;
    }

    private static void checkGroup(){
        if (sGroupMap.size() == 0){
            sGroupMap.put(READ_CALENDAR, Manifest.permission_group.CALENDAR);
            sGroupMap.put(WRITE_CALENDAR,Manifest.permission_group.CALENDAR);

            sGroupMap.put(CAMERA,Manifest.permission_group.CAMERA);

            sGroupMap.put(READ_CONTACTS,Manifest.permission_group.CONTACTS);
            sGroupMap.put(WRITE_CONTACTS,Manifest.permission_group.CONTACTS);
            sGroupMap.put(GET_ACCOUNTS,Manifest.permission_group.CONTACTS);

            sGroupMap.put(ACCESS_FINE_LOCATION,Manifest.permission_group.LOCATION);
            sGroupMap.put(ACCESS_COARSE_LOCATION,Manifest.permission_group.LOCATION);

            sGroupMap.put(RECORD_AUDIO,Manifest.permission_group.MICROPHONE);

            sGroupMap.put(READ_PHONE_STATE,Manifest.permission_group.PHONE);
            sGroupMap.put(CALL_PHONE,Manifest.permission_group.PHONE);
            sGroupMap.put(READ_CALL_LOG,Manifest.permission_group.PHONE);
            sGroupMap.put(WRITE_CALL_LOG,Manifest.permission_group.PHONE);
            sGroupMap.put(ADD_VOICEMAIL,Manifest.permission_group.PHONE);
            sGroupMap.put(USE_SIP,Manifest.permission_group.PHONE);
            sGroupMap.put(PROCESS_OUTGOING_CALLS,Manifest.permission_group.PHONE);

            sGroupMap.put(BODY_SENSORS,Manifest.permission_group.SENSORS);

            sGroupMap.put(SEND_SMS,Manifest.permission_group.SMS);
            sGroupMap.put(RECEIVE_SMS,Manifest.permission_group.SMS);
            sGroupMap.put(READ_SMS,Manifest.permission_group.SMS);
            sGroupMap.put(RECEIVE_WAP_PUSH,Manifest.permission_group.SMS);
            sGroupMap.put(RECEIVE_MMS,Manifest.permission_group.SMS);

            sGroupMap.put(READ_EXTERNAL_STORAGE,Manifest.permission_group.STORAGE);
            sGroupMap.put(WRITE_EXTERNAL_STORAGE,Manifest.permission_group.STORAGE);
        }
    }
}
