package droid.game.common.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class UIDialog extends UIBaseDialog{
    private Context mBaseContext;
    private List<OnDismissListener> mOnDismissListeners = new ArrayList<>();
    private List<OnCancelListener> mOnCancelListeners = new ArrayList<>();

    private OnDismissListener mOnDismissListener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            for (OnDismissListener onDismissListener : mOnDismissListeners){
                onDismissListener.onDismiss(dialog);
            }

            mOnDismissListeners.clear();
        }
    };

    public UIDialog(Context context){
        this(context,android.R.style.Theme_Dialog);
    }

    public UIDialog(Context context, int themeResId) {
        super(context, themeResId);

        mBaseContext = context;
        init();

        setOnDismissListener(mOnDismissListener);
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                for (OnCancelListener onCancelListener : mOnCancelListeners){
                    onCancelListener.onCancel(dialog);
                }

                mOnCancelListeners.clear();
            }
        });

    }

    public UIDialog(Context context,boolean inFragment){
        super(context,android.R.style.Theme_Dialog);
        mBaseContext = context;
        init();
    }

    private void init() {
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    public void showWithImmersiveCheck(Activity activity) {
        // http://stackoverflow.com/questions/22794049/how-to-maintain-the-immersive-mode-in-dialogs
        Window window = getWindow();
        if (window == null) {
            return;
        }

        Window activityWindow = activity.getWindow();
        int activitySystemUi = activityWindow.getDecorView().getSystemUiVisibility();
        if ((activitySystemUi & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) == View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN ||
                (activitySystemUi & View.SYSTEM_UI_FLAG_FULLSCREEN) == View.SYSTEM_UI_FLAG_FULLSCREEN) {
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            window.getDecorView().setSystemUiVisibility(
                    activity.getWindow().getDecorView().getSystemUiVisibility());
            super.show();
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        } else {
            super.show();
        }
    }

    public void showWithImmersiveCheck() {
        if (!(mBaseContext instanceof Activity)) {
            super.show();
            return;
        }
        Activity activity = (Activity) mBaseContext;
        showWithImmersiveCheck(activity);
    }

    public UIDialog addOnDismissListener(OnDismissListener listener){
        if (!mOnDismissListeners.contains(listener)){
            mOnDismissListeners.add(listener);
        }
        return this;
    }

    public UIDialog addOnCancelListener(OnCancelListener listener){
        if (!mOnCancelListeners.contains(listener)){
            mOnCancelListeners.add(listener);
        }
        return this;
    }
}