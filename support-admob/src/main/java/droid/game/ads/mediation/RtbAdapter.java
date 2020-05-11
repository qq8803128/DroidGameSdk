package droid.game.ads.mediation;


import droid.game.ads.mediation.rtb.RtbSignalData;
import droid.game.ads.mediation.rtb.SignalCallbacks;

public abstract class RtbAdapter extends Adapter{
    public abstract void collectSignals(RtbSignalData var1, SignalCallbacks var2);
}
