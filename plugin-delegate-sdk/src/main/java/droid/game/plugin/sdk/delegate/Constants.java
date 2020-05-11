package droid.game.plugin.sdk.delegate;

import android.content.Context;
import droid.game.plugin.manager.PluginManager;

public class Constants {
    public static final String PACKAGE = "droid.game.plugin.core";

    public static Context getSelfContext(){
        return PluginManager.manager().getLoadedApk(Constants.PACKAGE).getPluginContext();
    }
}
