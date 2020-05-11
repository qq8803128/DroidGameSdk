package droid.game.compiler.config;

import java.io.File;

public class Project {
    public String dir;
    public String sig;
    public Module app;
    public Module lib;

    public static Project create(String dir,String packageName){
        return new Project(dir,packageName);
    }

    private Project(String dir,String packageName) {
        File file = new File(dir);
        if (!file.exists() || !file.isDirectory()){
            file.mkdirs();
        }
        this.dir = dir;
        File sign = new File(file,"sig");
        if (!sign.exists() || !sign.isDirectory()){
            sign.mkdirs();
        }
        sig = sign.getAbsolutePath() + File.separator;

        app = new Module(dir,"app");
        lib = new Module(dir,packageName);
    }

    public static class Module{
        public String dir;
        public String libs;
        public String src;
        public String main;
        public String java;
        public String res;
        public String assets;
        public String jniLibs;
        public String drawable;
        public String values;

        public Module(String parent,String packageName){
            File file = new File(parent,packageName);
            if (!file.exists() || !file.isDirectory()){
                file.mkdirs();
            }
            dir = file.getAbsolutePath() + File.separator;
            libs = createDir(dir,"libs");
            src = createDir(dir,"src");
            main = createDir(src,"main");
            java = createDir(main,"java");
            res = createDir(main,"res");
            assets = createDir(main,"assets");
            jniLibs = createDir(main,"jniLibs");
            drawable = createDir(res,"drawable");
            values = createDir(res,"values");
        }
    }

    private static String createDir(String parent,String name){
        File f = new File(parent,name);
        if (!f.exists() || !f.isDirectory()){
            f.mkdirs();
        }
        return f.getAbsolutePath() + File.separator;
    }
}
