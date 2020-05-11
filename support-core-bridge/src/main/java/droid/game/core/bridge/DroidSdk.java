package droid.game.core.bridge;

import droid.game.core.bridge.bridge.BridgeSdk;
import droid.game.core.bridge.bridge.BrigeApp;
import droid.game.core.keep.IActivity;
import droid.game.core.keep.IApplication;
import droid.game.core.keep.IGame;

public final class DroidSdk {
    public static final IApplication Application = BrigeApp.application();
    public static final IGame Game = BridgeSdk.sdk();
    public static final IActivity Activity = BridgeSdk.sdk();
}

