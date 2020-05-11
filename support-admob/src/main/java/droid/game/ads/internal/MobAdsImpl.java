package droid.game.ads.internal;

import android.content.Context;
import droid.game.ads.initialization.InitializationStatus;
import droid.game.ads.initialization.OnInitializationCompleteListener;
import droid.game.ads.mediation.RtbAdapter;
import droid.game.ads.reward.RewardedVideoAd;

public class MobAdsImpl {

    public static void initialize(Context context, OnInitializationCompleteListener onInitializationCompleteListener) {

    }

    public static InitializationStatus getInitializationStatus() {
        return null;
    }

    public static void registerRtbAdapter(Class<? extends RtbAdapter> adapterClass){

    }

    public static RewardedVideoAd getRewardedVideoAdInstance(Context context) {
        return null;
    }

    public static void setAppVolume(float volume) {

    }

    public static void setAppMuted(boolean muted) {

    }

    public static void setTestMode(boolean test) {

    }

    public static void openDebugMode(boolean bebug) {

    }

    public static String getVersionString() {
        return null;
    }

}
