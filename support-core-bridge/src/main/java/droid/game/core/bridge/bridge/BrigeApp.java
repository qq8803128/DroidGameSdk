package droid.game.core.bridge.bridge;

import android.app.Application;
import android.content.res.Configuration;
import droid.game.common.suspen.Suspen;
import droid.game.common.global.Global;
import droid.game.common.log.LogRecord;
import droid.game.core.keep.IApplication;

public final class BrigeApp implements IApplication {
    public static final IApplication application() {
        return Holder.holder;
    }

    private static final class Holder {
        private static final IApplication holder = new BrigeApp();
    }

    private IApplication mDelegateApp;

    private BrigeApp() {
        super();
        LogRecord.d("BrigeApp create");
    }

    @Override
    public void attachBaseContext(Application app) {
        LogRecord.d("fingerApp::attachBaseContext[%s]", app);
        Global.setApplication(app);

        //init suspen window theme
        switch (Attribute.system().getSuspenTheme()) {
            case "Night":
                Suspen.initTheme(Suspen.Night);
                break;
            case "Light":
            default:
                Suspen.initTheme(Suspen.Light);
                break;
        }

        getDelegateApp().attachBaseContext(app);
    }

    @Override
    public void onCreate(Application app) {
        LogRecord.d("fingerApp::onCreate[%s]", app);
        getDelegateApp().onCreate(app);
    }

    @Override
    public void onTerminate(Application app) {
        LogRecord.d("fingerApp::onTerminate[%s]", app);
        getDelegateApp().onTerminate(app);
    }

    @Override
    public void onConfigurationChanged(Application app, Configuration newConfig) {
        LogRecord.d("fingerApp::onConfigurationChanged[%s,%s]", app, newConfig);
        getDelegateApp().onConfigurationChanged(app, newConfig);
    }

    @Override
    public void onLowMemory(Application app) {
        LogRecord.d("fingerApp::onLowMemory[%s]", app);
        getDelegateApp().onLowMemory(app);
    }

    @Override
    public void onTrimMemory(Application app, int level) {
        LogRecord.d("fingerApp::onTrimMemory[%s,%d]", app, level);
        getDelegateApp().onTrimMemory(app, level);
    }

    private IApplication getDelegateApp() {
        if (mDelegateApp == null) {
            mDelegateApp = BrigeDelegate.create(Attribute.system().getChannelApp(), new Class[]{IApplication.class});
        }
        return mDelegateApp;
    }
}
