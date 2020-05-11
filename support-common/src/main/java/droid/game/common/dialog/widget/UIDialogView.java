package droid.game.common.dialog.widget;

import android.content.Context;
import android.graphics.Canvas;

public class UIDialogView extends UIDialogLayoutView{
    private OnDecorationListener mOnDecorationListener;

    public UIDialogView(Context context) {
        super(context);
    }

    public void setOnDecorationListener(OnDecorationListener onDecorationListener) {
        mOnDecorationListener = onDecorationListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mOnDecorationListener != null) {
            mOnDecorationListener.onDraw(canvas, this);
        }
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mOnDecorationListener != null) {
            mOnDecorationListener.onDrawOver(canvas, this);
        }
    }

    public interface OnDecorationListener {
        void onDraw(Canvas canvas, UIDialogView view);

        void onDrawOver(Canvas canvas, UIDialogView view);
    }
}
