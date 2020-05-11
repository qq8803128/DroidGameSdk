package droid.game.compiler.config;

import static droid.game.compiler.util.FileUtils.*;

public class Bin {
    private static String apktool;
    private static String dex2jar;
    private static String enjarify;

    static {
        Data data = json(getConfigDir(),"bin.json",Data.class);
        apktool = getDir("tools",data.apktool.dir) + data.apktool.name;
        dex2jar = getDir("tools",data.dex2jar.dir) + data.dex2jar.name;
        enjarify = getDir("tools",data.enjarify.dir) + data.enjarify.name;
    }

    public static String getApktool(){
        return apktool;
    }

    public static String getDex2jar(){
        return dex2jar;
    }

    public static String getEnjarify(){
        return enjarify;
    }

    public static class Data{
        private Item apktool;
        private Item dex2jar;
        private Item enjarify;
    }

    public static class Item{
        private String dir;
        private String name;
    }

}
