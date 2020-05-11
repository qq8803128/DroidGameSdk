package droid.game.ads;

import android.content.Context;
import droid.game.ads.initialization.InitializationStatus;
import droid.game.ads.initialization.OnInitializationCompleteListener;
import droid.game.ads.internal.MobAdsImpl;
import droid.game.ads.mediation.RtbAdapter;
import droid.game.ads.reward.RewardedVideoAd;

public class MobAds {
    public static void initialize(Context context) {
        initialize(context,null);
    }

    public static void initialize(Context context, OnInitializationCompleteListener onInitializationCompleteListener) {
        MobAdsImpl.initialize(context,onInitializationCompleteListener);
    }

    public static InitializationStatus getInitializationStatus() {
        return MobAdsImpl.getInitializationStatus();
    }

    public static void registerRtbAdapter(Class<? extends RtbAdapter> adapterClass){
        MobAdsImpl.registerRtbAdapter(adapterClass);
    }

    public static RewardedVideoAd getRewardedVideoAdInstance(Context context) {
        return MobAdsImpl.getRewardedVideoAdInstance(context);
    }

    public static void setAppVolume(float volume) {
        MobAdsImpl.setAppVolume(volume);
    }

    public static void setAppMuted(boolean muted) {
        MobAdsImpl.setAppMuted(muted);
    }

    public static void setTestMode(boolean test) {
        MobAdsImpl.setTestMode(test);
    }

    public static void openDebugMode(boolean bebug) {
        MobAdsImpl.openDebugMode(bebug);
    }

    public static String getVersionString() {
        return MobAdsImpl.getVersionString();
    }

}
