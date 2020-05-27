package droid.game.android.floater.window;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class FtContainer extends FrameLayout {
    public FtContainer(Context context) {
        this(context,null);
    }

    public FtContainer(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FtContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
