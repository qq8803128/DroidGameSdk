package droid.game.ads.mediation;

import android.os.Bundle;
import droid.game.ads.AdFormat;

public class MediationConfiguration {
    private final AdFormat adFormat;
    private final Bundle serverParameters;

    public MediationConfiguration(AdFormat adFormat, Bundle serverParameters) {
        this.adFormat = adFormat;
        this.serverParameters = serverParameters;
    }

    public AdFormat getFormat() {
        return this.adFormat;
    }

    public Bundle getServerParameters() {
        return this.serverParameters;
    }
}
