package droid.game.ads.mediation.rtb;

import android.content.Context;
import android.os.Bundle;
import droid.game.ads.AdSize;
import droid.game.ads.mediation.MediationConfiguration;

import java.util.List;

public class RtbSignalData {
    private final Context mContext;
    private final List<MediationConfiguration> mConfigurations;
    private final Bundle mBundle;
    private final AdSize mAdSize;

    public RtbSignalData(Context var1, List<MediationConfiguration> var2, Bundle var3, AdSize var4) {
        this.mContext = var1;
        this.mConfigurations = var2;
        this.mBundle = var3;
        this.mAdSize = var4;
    }

    public Context getContext() {
        return this.mContext;
    }

    /** @deprecated */
    @Deprecated
    public MediationConfiguration getConfiguration() {
        return this.mConfigurations != null && this.mConfigurations.size() > 0 ? (MediationConfiguration)this.mConfigurations.get(0) : null;
    }

    public List<MediationConfiguration> getConfigurations() {
        return this.mConfigurations;
    }

    public Bundle getNetworkExtras() {
        return this.mBundle;
    }

    public AdSize getAdSize() {
        return this.mAdSize;
    }
}
