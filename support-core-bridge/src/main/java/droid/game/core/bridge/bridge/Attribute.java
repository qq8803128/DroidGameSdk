package droid.game.core.bridge.bridge;

import droid.game.common.global.Global;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * android game sdk assets android.gamecenter.system get
 */
public class Attribute {
    private static System sSystem;
    private static Game sGame;
    private static Map<String, Attribute> sAttributeMap = Collections.synchronizedMap(new HashMap<String, Attribute>());

    public static final System system() {
        if(sGame == null){
            sGame = new Game(get("android.gamecenter.system/game.ini"));
        }
        if (sSystem == null) {
            sSystem = new System(get("android.gamecenter.system/system.ini"));
        }
        return sSystem;
    }

    public final static Attribute read(String fileName) {
        try {
            if (sAttributeMap.get(fileName) == null) {
                sAttributeMap.put(fileName, new Attribute(get(fileName)));
            }
            return sAttributeMap.get(fileName);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private Properties mProperties;

    public Attribute(InputStream is) {
        super();
        try {
            if (is != null) {
                mProperties = new Properties();
                mProperties.load(is);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取assets文件夹下面ini文件的属性
     *
     * @param key
     * @param defVal
     * @return
     */
    public final String get(String key, String defVal) {
        try {
            if (mProperties != null) {
                return mProperties.getProperty(key, defVal);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return defVal;
    }

    /**
     * 获取属性集合
     *
     * @return
     */
    public final Properties getProperties() {
        return mProperties;
    }

    private static InputStream get(String fileName) {
        try {
            return Global.getApplication().getAssets().open(fileName);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public final static class System extends Attribute {
        public System(InputStream is) {
            super(is);
        }

        public final String getAppId() {
            return sGame.getAppId();
        }

        public final boolean isOnlineGame(){
            return sGame.isOnlineGame();
        }

        public final String getChannelApp() {
            return get("CHANNEL_APP", "droid.game.xsdk.inner.InnerApp");
        }

        public final String getChannelSdk() {
            return get("CHANNEL_SDK", "droid.game.xsdk.inner.InnerSdk");
        }

        public final String getPermissionManager(){
            return get("PERMISSION_MANAGER", "droid.game.permission.runtime.PermissionManagerImpl");
        }

        public final String getSuspenTheme() {
            return get("SUSPEN_THEME", "Light");
        }

        public final boolean isFloaterShow() {
            boolean result = true;
            switch (get("FLOATER_SHOW", "true")) {
                case "FALSE":
                case "False":
                case "false":
                    result = false;
                    break;
                default:
                    result = true;
                    break;

            }
            return result;
        }

        public final String getFloaterIcon() {
            return get("FLOATER_ICON", "");
        }

        public final String getSplashStyle() {
            return get("SPLASH_STYLE", "Null");
        }

        public final String getSplashIcon() {
            return get("SPLASH_ICON", "");
        }

        public final long getSplashDuration() {
            try {
                return Long.parseLong(get("SPLASH_DURATION", "0"));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return 0L;
        }

        public final String getSplashScaleType() {
            return get("SPLASH_SCALE_TYPE", "");
        }

        public final String getHost() {
            return get("HOST", "https://api.3722game.com");
        }

        public final boolean checkUpdate() {
            boolean result = true;
            switch (get("UPDATE", "true")) {
                case "FALSE":
                case "False":
                case "false":
                    result = false;
                    break;
                default:
                    result = true;
                    break;
            }
            return result;
        }

        public boolean multidexInstall() {
            boolean result = false;
            switch (get("MULTIDEX", "false")) {
                case "FALSE":
                case "False":
                case "false":
                    result = false;
                    break;
                default:
                    result = true;
                    break;
            }
            return result;
        }

        public boolean debug(){
            boolean result = false;
            switch (get("DEBUG", "false")) {
                case "FALSE":
                case "False":
                case "false":
                    result = false;
                    break;
                default:
                    result = true;
                    break;
            }
            return result;
        }
    }

    final static class Game extends Attribute{
        public Game(InputStream is) {
            super(is);
        }

        public final String getAppId() {
            return get("APP_ID", "0");
        }

        public final boolean isOnlineGame(){
            boolean result = true;
            switch (get("ONLINE_GAME","true")){
                case "FALSE":
                case "False":
                case "false":
                    result = false;
                    break;
                default:
                    result = true;
                    break;
            }
            return result;
        }

    }

}
