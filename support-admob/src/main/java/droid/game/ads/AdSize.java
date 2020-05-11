package droid.game.ads;

public final class AdSize {
    public static final int FULL_WIDTH = -1;
    public static final int AUTO_HEIGHT = -2;

    private final int width;
    private final int height;

    public AdSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
