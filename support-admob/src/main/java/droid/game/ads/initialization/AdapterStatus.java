package droid.game.ads.initialization;

public interface AdapterStatus {
    AdapterStatus.State getInitializationState();

    String getDescription();

    int getLatency();

    public static enum State {
        NOT_READY,
        READY;

        private State() {
        }
    }
}
