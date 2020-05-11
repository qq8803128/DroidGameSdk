package droid.game.x2c;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

/**
 * @author：chengwei 2018/8/9
 * @description
 */
public interface IViewCreator {
    View createView(Context context, Context cloneInContext);
}