package droid.game.common.link;

import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.method.Touch;
import android.view.MotionEvent;
import android.widget.TextView;
import droid.game.common.util.UILinkTouchDecorHelper;

/**
 * 配合 {@link UILinkTouchDecorHelper} 使用
 *
 * @author cginechen
 * @date 2017-03-20
 */

public class UILinkTouchMovementMethod extends LinkMovementMethod {

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        return sHelper.onTouchEvent(widget, buffer, event)
                || Touch.onTouchEvent(widget, buffer, event);
    }

    public static MovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new UILinkTouchMovementMethod();

        return sInstance;
    }

    private static UILinkTouchMovementMethod sInstance;
    private static UILinkTouchDecorHelper sHelper = new UILinkTouchDecorHelper();

}
