package droid.game.plugin.sdk.delegate.ui.online.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import droid.game.butterknife.ButterKnife;
import droid.game.butterknife.IContextProvider;
import droid.game.butterknife.Unbinder;
import droid.game.common.dialog.UIDialog;
import droid.game.common.dialog.UIDialogBuilder;
import droid.game.common.dialog.widget.UIDialogRootLayout;
import droid.game.common.dialog.widget.UIDialogView;
import droid.game.common.util.UIDisplayHelper;
import droid.game.common.util.UIResourcesHelper;
import droid.game.plugin.sdk.delegate.Constants;
import droid.game.x2c.X2C;

public abstract class Dialog<T extends Dialog> extends UIDialogBuilder<T> implements IContextProvider {
    protected abstract String getLayout();

    private Unbinder mUnbinder;
    public Dialog(Context context) {
        this(context,0.8f);
    }

    public Dialog(Context context, float percentWidth) {
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

    @Override
    public void onAfterCreate(UIDialog dialog, UIDialogRootLayout rootLayout, Context context) {
        super.onAfterCreate(dialog, rootLayout, context);
        mUnbinder = ButterKnife.bind(this,dialog);

        dialog.addOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onDestroy();
            }
        });
    }

    public void onDestroy(){
        mUnbinder.unbind();
    }

    protected View inflate(String layoutId){
        return X2C.inflate(
                getBaseContext(),
                Constants.getSelfContext(),
                UIResourcesHelper.getLayout(
                        Constants.getSelfContext(),
                        layoutId
                ),
                null);
    }

    @Override
    public Context provideContext() {
        return Constants.getSelfContext();
    }

    @Override
    public View onCreateContentView(UIDialog dialog, UIDialogView dialogView, Context context) {
        return inflate(getLayout());
    }
}
