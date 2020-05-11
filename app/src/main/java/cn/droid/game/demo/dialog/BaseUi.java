package cn.droid.game.demo.dialog;

import android.content.Context;
import droid.game.common.dialog.UIDialogBuilder;
import droid.game.common.util.UIDisplayHelper;

public abstract class BaseUi extends UIDialogBuilder<BaseUi> {
    public BaseUi(Context context) {
        this(context,0.8f);
    }

    public BaseUi(Context context, float percentWidth) {
        super(context);
        setPercent(1.0f);
        setLayout(calcWidth(percentWidth),calcWidth(percentWidth),0,0);
    }

    private int calcWidth(float percentWidth){
        int w = UIDisplayHelper.getScreenWidth(getBaseContext());
        int h = UIDisplayHelper.getScreenHeight(getBaseContext());
        int width = (int) (w * percentWidth);
        if (w > h){
            width = (int) (h * percentWidth);
        }
        return width;
    }

}
