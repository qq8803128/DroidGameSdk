package droid.game.permission.util;

import android.content.Context;

import java.io.InputStream;
import java.util.List;

public class PermissionUtils {
    public static int getTargetSdkVersion(Context context) {
        return context.getApplicationInfo().targetSdkVersion;
    }

    public static String[] array(List<String> list){
        if (list == null || list.size() == 0){
            return new String[]{};
        }else{
            String[] array = new String[list.size()];
            list.toArray(array);
            return array;
        }
    }

    public static String open(Context context,String path){
        InputStream is = null;
        String json = "";
        try{
            is = context.getAssets().open(path);
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            json = new String(bytes);
        }catch (Throwable e){
            e.printStackTrace();
        }finally {
            closeIo(is);
        }
        return json;
    }

    private static void closeIo(InputStream is){
        try{
            if (is != null){
                is.close();
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}
