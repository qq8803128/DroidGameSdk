package android.content.res;


/**
 * Created by qiaopu on 2018/5/3.
 */
public final class ResourcesKey {
    public final String mResDir;


    public final String[] mSplitResDirs;


    public final String[] mOverlayDirs;


    public final String[] mLibDirs;

    public final int mDisplayId;


    public final Configuration mOverrideConfiguration;


    public final CompatibilityInfo mCompatInfo;

    public ResourcesKey(String resDir, String[] splitResDirs, String[] overlayDirs, String[] libDirs, int displayId, Configuration overrideConfig, CompatibilityInfo compatInfo) {
        throw new RuntimeException("Stub!");
    }

}

