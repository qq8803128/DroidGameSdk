package droid.game.ads.mediation;

import android.content.Context;
import com.google.android.gms.ads.mediation.MediationAdLoadCallback;
import com.google.android.gms.ads.mediation.MediationRewardedAd;
import com.google.android.gms.ads.mediation.MediationRewardedAdCallback;
import com.google.android.gms.ads.mediation.MediationRewardedAdConfiguration;

import java.util.List;

public abstract class Adapter implements MediationExtrasReceiver{
    public abstract void initialize(Context context, InitializationCompleteCallback callback, List<MediationConfiguration> configurations);

    public void loadRewardedAd(MediationRewardedAdConfiguration configuration, MediationAdLoadCallback<MediationRewardedAd, MediationRewardedAdCallback> callbackMediationAdLoadCallback) {
        callbackMediationAdLoadCallback.onFailure(String.valueOf(this.getClass().getSimpleName()).concat(" does not support rewarded ads."));
    }

    public abstract VersionInfo getVersionInfo();

    public abstract VersionInfo getSDKVersionInfo();
}
