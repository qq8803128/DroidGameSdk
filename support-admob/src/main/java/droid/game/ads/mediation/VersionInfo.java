package droid.game.ads.mediation;

public final class VersionInfo {
    private final int majorVersion;
    private final int minorVersion;
    private final int microVersion;

    public VersionInfo(int majorVersion, int minorVersion, int microVersion) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.microVersion = microVersion;
    }

    public final int getMajorVersion() {
        return this.majorVersion;
    }

    public final int getMinorVersion() {
        return this.minorVersion;
    }

    public final int getMicroVersion() {
        return this.microVersion;
    }
}
