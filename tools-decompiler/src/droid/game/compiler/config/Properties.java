package droid.game.compiler.config;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;

import java.io.File;
import java.io.IOException;

public class Properties {
    private static ApkFile sApkFile;
    private static ApkMeta sApkMeta;

    static {
        try {
            sApkFile = new ApkFile(Inputs.getInput());
            sApkMeta = sApkFile.getApkMeta();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String applicationId = sApkMeta.getPackageName();
    public static String minSdkVersion = sApkMeta.getMinSdkVersion();
    public static String targetSdkVersion = sApkMeta.getTargetSdkVersion();
    public static String versionCode = sApkMeta.getVersionCode().toString();
    public static String versionName = sApkMeta.getVersionName();
    public static String library = ":" + applicationId;
    public static String abiFilters = getAbiFilters();
    public static String multiDexEnabled = getMultidexEnabled();

    private static String getMultidexEnabled() {
        String dir = Inputs.getTempLibs();
        File libs = new File(dir);
        int dexCount = 0;
        for (File file : libs.listFiles()){
            if (file.isFile() && file.getName().startsWith("classes")){
                dexCount++;
            }
        }
        return (dexCount >= 2) + "";
    }

    private static String getAbiFilters() {
        String abiFilter = "";
        String lib = Inputs.getTempJni();//getDir(TEMP,Global.getDecompileApk().getName(),"lib");
        File jniLibs = new File(lib);
        if (jniLibs.isDirectory() && jniLibs.exists()){
            for (File f : jniLibs.listFiles()){
                if (f.isDirectory()){
                    abiFilter += "'" + f.getName() + "',";
                }
            }
        }
        if (abiFilter == null || abiFilter.length() == 0){
            abiFilter = "'arm64-v8a','armeabi','armeabi-v7a','mips','mips64','x86','x86_64',";
        }
        abiFilter = abiFilter.substring(0,abiFilter.length() - 1);
        return abiFilter;
    }
}
