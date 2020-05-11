package droid.game.ads.reward;

import android.content.Context;
import android.os.Bundle;
import droid.game.ads.AdRequest;
import droid.game.ads.ResponseInfo;

public interface RewardedVideoAd {
    void loadAd(String unionId, AdRequest adRequest);

    boolean isLoaded();

    void show();

    void setRewardedVideoAdListener(RewardedVideoAdListener var1);

    void setAdMetadataListener(AdMetadataListener var1);

    Bundle getAdMetadata();

    RewardedVideoAdListener getRewardedVideoAdListener();

    String getUserId();

    void pause(Context var1);

    void resume(Context var1);

    void destroy(Context var1);

    String getMediationAdapterClassName();

    void setImmersiveMode(boolean immersiveMode);

    void setCustomData(String var1);

    String getCustomData();

    ResponseInfo getResponseInfo();
}
