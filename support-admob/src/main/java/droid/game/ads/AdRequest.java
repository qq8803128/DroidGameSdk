package droid.game.ads;

public class AdRequest {
    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 2;

    public AdRequest setUserID(String userId){
        return this;
    }
    public AdRequest setRewardName(String rewardName){
        return this;
    }
    public AdRequest setRewardAmount(int amount){
        return this;
    }

    /*
    public AdRequest setAdSize(AdSize adSize){
        return this;
    }
    */

    public AdRequest setMediaExtra(String extra){
        return this;
    }

    public AdRequest setOrientation(int orientation){
        return this;
    }
}
