package droid.game.core.bridge.bridge;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import droid.game.common.bus.BusProvider;
import droid.game.common.global.Global;
import droid.game.common.keep.Consumer;
import droid.game.common.log.LogRecord;
import droid.game.core.bridge.splash.Splash;
import droid.game.core.keep.IChannel;
import droid.game.core.keep.IEventReceiver;
import droid.game.core.keep.ISdk;
import droid.game.core.parameter.Parameter;
import droid.game.core.result.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BridgeSdk implements ISdk {
    public static final ISdk sdk(){
        return Holder.holder;
    }

    private static final class Holder{
        private static final ISdk holder = new BridgeSdk();
    }

    private IChannel mDelegateSdk;
    private List<IEventReceiver> mReceivers = Collections.synchronizedList(new ArrayList<IEventReceiver>());

    private BridgeSdk() {
        super();
        LogRecord.d("BridgeSdk create");
    }

    @Override
    public void registerSDKEventReceiver(IEventReceiver eventReceiver) {
        LogRecord.d("BridgeSdk::registerSDKEventReceiver[%s]",eventReceiver);
        if (!mReceivers.contains(eventReceiver)){
            mReceivers.add(eventReceiver);
            BusProvider.get().register(eventReceiver);
        }
    }

    @Override
    public void unregisterSDKEventReceiver(IEventReceiver eventReceiver) {
        LogRecord.d("BridgeSdk::unregisterSDKEventReceiver[%s]",eventReceiver);
        if (mReceivers.contains(eventReceiver)){
            mReceivers.remove(eventReceiver);
            BusProvider.get().unregister(eventReceiver);
        }
    }

    @Override
    public void init(final Activity activity, final Parameter parameter) {
        LogRecord.d("BridgeSdk::init[%s,%s]",activity,parameter);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                Splash.splash().show(activity, new Runnable() {
                    @Override
                    public void run() {
                        getDelegateSdk().init(activity, parameter, new Consumer<Result>() {
                            @Override
                            public void accept(Result result) {
                                BusProvider.get().post(result);
                            }
                        }, new Consumer<Result>() {
                            @Override
                            public void accept(Result result) {
                                BusProvider.get().post(result);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void login(final Activity activity, final Parameter parameter) {
        LogRecord.d("BridgeSdk::login[%s,%s]",activity,parameter);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().login(activity, parameter, new Consumer<Result>() {
                    @Override
                    public void accept(Result result) {
                        BusProvider.get().post(result);
                    }
                }, new Consumer<Result>() {
                    @Override
                    public void accept(Result result) {
                        BusProvider.get().post(result);
                    }
                });
            }
        });
    }

    @Override
    public void logout(final Activity activity, final Parameter parameter) {
        LogRecord.d("BridgeSdk::logout[%s,%s]",activity,parameter);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().logout(activity, parameter, new Consumer<Result>() {
                    @Override
                    public void accept(Result result) {
                        BusProvider.get().post(result);
                    }
                }, new Consumer<Result>() {
                    @Override
                    public void accept(Result result) {
                        BusProvider.get().post(result);
                    }
                });
            }
        });
    }

    @Override
    public void role(final Activity activity, final Parameter parameter) {
        LogRecord.d("BridgeSdk::role[%s,%s]",activity,parameter);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().role(activity, parameter, new Consumer<Result>() {
                    @Override
                    public void accept(Result result) {
                        BusProvider.get().post(result);
                    }
                }, new Consumer<Result>() {
                    @Override
                    public void accept(Result result) {
                        BusProvider.get().post(result);
                    }
                });
            }
        });
    }

    @Override
    public void pay(final Activity activity, final Parameter parameter) {
        LogRecord.d("BridgeSdk::pay[%s,%s]",activity,parameter);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().pay(activity, parameter, new Consumer<Result>() {
                    @Override
                    public void accept(Result result) {
                        BusProvider.get().post(result);
                    }
                }, new Consumer<Result>() {
                    @Override
                    public void accept(Result result) {
                        BusProvider.get().post(result);
                    }
                });
            }
        });
    }

    @Override
    public void exit(final Activity activity, final Parameter parameter) {
        LogRecord.d("BridgeSdk::exit[%s,%s]",activity,parameter);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().exit(activity, parameter, new Consumer<Result>() {
                    @Override
                    public void accept(Result result) {
                        BusProvider.get().post(result);
                    }
                }, new Consumer<Result>() {
                    @Override
                    public void accept(Result result) {
                        BusProvider.get().post(result);
                    }
                });
            }
        });
    }

    @Override
    public void exec(final Activity activity, final Parameter parameter) {
        LogRecord.d("BridgeSdk::exec[%s,%s]",activity,parameter);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().exec(activity, parameter, new Consumer<Result>() {
                    @Override
                    public void accept(Result result) {
                        BusProvider.get().post(result);
                    }
                }, new Consumer<Result>() {
                    @Override
                    public void accept(Result result) {
                        BusProvider.get().post(result);
                    }
                });
            }
        });
    }

    @Override
    public void exec(final Activity activity, final Parameter parameter, final IResult resultCreator) {
        LogRecord.d("BridgeSdk::exec[%s,%s,%s]",activity,parameter,resultCreator);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().exec(activity, parameter, new Consumer<Result>() {
                    @Override
                    public void accept(Result result) {
                        if (resultCreator == null){
                            BusProvider.get().post(result);
                        }else{
                            BusProvider.get().post(resultCreator.onResult(result));
                        }
                    }
                }, new Consumer<Result>() {
                    @Override
                    public void accept(Result result) {
                        if (resultCreator == null){
                            BusProvider.get().post(result);
                        }else{
                            BusProvider.get().post(resultCreator.onResult(result));
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onCreate(final Activity activity) {
        LogRecord.d("BridgeSdk::onCreate[%s]",activity);
        Global.setActivity(activity);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().onCreate(activity);
            }
        });
    }

    @Override
    public void onResume(final Activity activity) {
        LogRecord.d("BridgeSdk::onResume[%s]",activity);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().onResume(activity);
            }
        });
    }

    @Override
    public void onRestart(final Activity activity) {
        LogRecord.d("BridgeSdk::onRestart[%s]",activity);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().onRestart(activity);
            }
        });
    }

    @Override
    public void onStart(final Activity activity) {
        LogRecord.d("BridgeSdk::onStart[%s]",activity);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().onStart(activity);
            }
        });
    }

    @Override
    public void onPause(final Activity activity) {
        LogRecord.d("BridgeSdk::onPause[%s]",activity);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().onPause(activity);
            }
        });
    }

    @Override
    public void onStop(final Activity activity) {
        LogRecord.d("BridgeSdk::onStop[%s]",activity);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().onStop(activity);
            }
        });
    }

    @Override
    public void onDestroy(final Activity activity) {
        LogRecord.d("BridgeSdk::onDestroy[%s]",activity);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().onDestroy(activity);
            }
        });
    }

    @Override
    public void onNewIntent(final Activity activity, final Intent intent) {
        LogRecord.d("BridgeSdk::onNewIntent[%s,%s]",activity,intent);
        Global.setActivity(null);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().onNewIntent(activity,intent);
            }
        });
    }

    @Override
    public void onConfigurationChanged(final Activity activity, final Configuration newConfig) {
        LogRecord.d("BridgeSdk::onConfigurationChanged[%s,%s]",activity,newConfig);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().onConfigurationChanged(activity,newConfig);
            }
        });
    }

    @Override
    public void onActivityResult(final Activity activity, final int requestCode, final int resultCode, final Intent data) {
        LogRecord.d("BridgeSdk::onActivityResult[%s,%d,%d,%s]",activity,requestCode,resultCode,data);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().onActivityResult(activity,requestCode,resultCode,data);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(final Activity activity, final int requestCode, final String[] permissions, final int[] grantResults) {
        LogRecord.d("BridgeSdk::onRequestPermissionsResult[%s,%d,%s,%s]",activity,requestCode,permissions,grantResults);

        runOnUiThread(activity, new Runnable() {
            @Override
            public void run() {
                getDelegateSdk().onRequestPermissionsResult(activity,requestCode,permissions,grantResults);
            }
        });
    }

    private IChannel getDelegateSdk(){
        if (mDelegateSdk == null){
            mDelegateSdk = BrigeDelegate.create(Attribute.system().getChannelSdk(),new Class[]{IChannel.class});
        }
        return mDelegateSdk;
    }

    private void runOnUiThread(Activity activity,Runnable runnable){
        activity.runOnUiThread(runnable);
    }
}
