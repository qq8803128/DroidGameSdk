package droid.game.compiler.config;


import droid.game.compiler.util.FileUtils;

import java.io.File;

public class Inputs {
    private static String input;

    public static void setInput(String in){
        input = in;
    }

    public static String getInput(){
        return input;
    }

    public static String getName(){
        return new File(input).getName().replace(".apk","");
    }

    public static String getTempout(){
        String path = FileUtils.getTempDir() + new File(input).getName().replace(".apk","") ;
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdirs();
        }
        return path;
    }

    public static String getTempLibs(){
        String path = FileUtils.getTempDir() + new File(input).getName().replace(".apk","") + File.separator + "libs" + File.separator;
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdirs();
        }
        return path;
    }

    public static String getTempDexs(){
        String path = FileUtils.getTempDir() + new File(input).getName().replace(".apk","") + File.separator + "dex" + File.separator;
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdirs();
        }
        return path;
    }

    public static String getTempRes(){
        String path = FileUtils.getTempDir() + new File(input).getName().replace(".apk","") + File.separator + "res" + File.separator;
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdirs();
        }
        return path;
    }

    public static String getTempJni(){
        String path = FileUtils.getTempDir() + new File(input).getName().replace(".apk","") + File.separator + "lib" + File.separator;
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdirs();
        }
        return path;
    }

    public static String getTempAssets(){
        String path = FileUtils.getTempDir() + new File(input).getName().replace(".apk","") + File.separator + "assets" + File.separator;
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdirs();
        }
        return path;
    }

    public static String getTempUnknown(){
        String path = FileUtils.getTempDir() + new File(input).getName().replace(".apk","") + File.separator + "unknown" + File.separator;
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdirs();
        }
        return path;
    }

    public static String getTempProject(){
        String path = FileUtils.getTempDir() + new File(input).getName().replace(".apk","") + File.separator + "as-proj" + File.separator;
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdirs();
        }
        return path;
    }
}
