package android.content;

import android.net.Uri;
import android.os.Bundle;

/**
 * Created by qiaopu on 2018/5/7.
 */
public abstract class ContentResolver {

    public ContentResolver(Context context) {
        throw new RuntimeException("Stub!");
    }

    public final Bundle call(Uri uri, String method, String arg, Bundle extras) {
        throw new RuntimeException("Stub!");
    }

    protected abstract IContentProvider acquireProvider(Context c, String name);

    protected IContentProvider acquireExistingProvider(Context c, String name) {
        throw new RuntimeException("Stub!");
    }

    public abstract boolean releaseProvider(IContentProvider icp);

    protected abstract IContentProvider acquireUnstableProvider(Context c, String name);

    public abstract boolean releaseUnstableProvider(IContentProvider icp);

    public abstract void unstableProviderDied(IContentProvider icp);

    public void appNotRespondingViaProvider(IContentProvider icp) {
        throw new RuntimeException("Stub!");
    }
}
