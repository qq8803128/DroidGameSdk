package droid.game.compiler.util;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;

public class FileUtils {
    public static String getWorkSpace(){
        String path = FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            path = URLDecoder.decode(path,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new File(path).getParentFile().getAbsolutePath() + File.separator + "WorkSpace" + File.separator;
    }

    public static String getJarDir(){
        String path = FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            path = URLDecoder.decode(path,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new File(path).getParentFile().getAbsolutePath() + File.separator;
    }

    public static String getDir(String... names){
        String dir = "";
        for (int i = 0; i < names.length; i++){
            String n = names[i];
            if (n != null && n.length() > 0){
                dir += (n + File.separator);
            }
        }
        String dir1 = getWorkSpace() + dir;
        File d = new File(dir1);
        if (!d.exists()){
            d.mkdirs();
        }
        return dir1;
    }

    public static String getToolsDir(){
        return getDir("tools");
    }

    public static String getConfigDir(){
        return getDir("config");
    }

    public static String getTempDir(){
        return getDir("output","temp");
    }

    public static String getSignDir(){
        return getDir("keystore");
    }

    public static String getProjDir(String version){
        return getDir("android",version);
    }

    public static <T> T json(String dir,String name,Class<T> clazz){
        try {
            Gson gson = new Gson();
            return gson.fromJson(read(dir, name), clazz);
        }catch (Throwable e){

        }
        return null;
    }

    public static <T> T json(String dir, String name, Type type){
        try {
            Gson gson = new Gson();
            return gson.fromJson(read(dir, name), type);
        }catch (Throwable e){

        }
        return null;
    }

    public static String read(String dir,String name){
        try {
            File file = new File(dir,name);
            StringBuffer buffer = new StringBuffer();
            FileInputStream is = new FileInputStream(file);
            byte[] temp = new byte[1024];

            int byteread = 0;
            while ((byteread = is.read(temp)) != -1) {
                String str = new String(temp, 0, byteread);
                buffer.append(str);
            }
            return buffer.toString();
        }catch (Throwable e){
            //e.printStackTrace();
        }
        return "";
    }

}
