package droid.game.common.device;

public class OaidProvider {
    private static boolean sSupport = false;
    private static String sOaid = "";
    private static String sAaid = "";
    private static String sVaid = "";

    public static void init(boolean support,String oaid,String aaid,String vaid){
        sSupport = support;
        sOaid = oaid;
        sAaid = aaid;
        sVaid = vaid;
    }

    public static boolean isSupport(){
        return sSupport;
    }

    public static String getOaid(){
        return sOaid;
    }

    public static String getAaid(){
        return sAaid;
    }

    public static String getVaid(){
        return sVaid;
    }
}
