package droid.game.core.bridge.splash;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import droid.game.common.concat.Concat;
import droid.game.common.dialog.IOSDialog;
import droid.game.common.dialog.UIDialog;
import droid.game.common.dialog.UIDialogBuilder;
import droid.game.common.dialog.widget.UIDialogRootLayout;
import droid.game.common.dialog.widget.UIDialogView;
import droid.game.common.keep.Consumer;
import droid.game.common.permission.PermissionManager;
import droid.game.common.toast.ToastEx;
import droid.game.common.util.UIDisplayHelper;
import droid.game.core.bridge.bridge.Attribute;
import droid.game.open.source.picasso.Picasso;
import droid.game.open.source.picasso.RequestCreator;
import droid.game.plugin.manager.PluginManager;

import java.util.ArrayList;

import static droid.game.common.util.UIThreadHelper.runDelay;


public class SplashDialog extends UIDialogBuilder<SplashDialog> {
    private ImageView mImageView;
    private UIDialog mLoadingDialog;
    private long mDuration;

    public SplashDialog(Context context) {
        super(context);
        setLayout(UIDisplayHelper.getScreenWidth(context), UIDisplayHelper.getScreenWidth(context), 0, 0);
        setPercent(1.0f);
    }

    @Override
    public void configDialogRootLayout(UIDialogRootLayout dialogRootLayout) {
        super.configDialogRootLayout(dialogRootLayout);
        dialogRootLayout.setPadding(0, 0, 0, 0);
    }

    @Override
    public void configDialogView(UIDialogView dialogView) {
        super.configDialogView(dialogView);
        dialogView.setPadding(0, 0, 0, 0);
    }

    @Override
    public View onCreateContentView(UIDialog dialog, UIDialogView dialogView, Context context) {
        ImageView imageView = new ImageView(context);
        mImageView = imageView;
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public LinearLayout.LayoutParams onCreateContentViewParams(Context context) {
        return new LinearLayout.LayoutParams(UIDisplayHelper.getScreenWidth(context), UIDisplayHelper.getScreenHeight(context));
    }

    @Override
    public void onAfterCreate(UIDialog dialog, UIDialogRootLayout rootLayout, Context context) {
        super.onAfterCreate(dialog, rootLayout, context);
        dialog.setFullScreen();
        setup();
    }

    protected ImageView getImageView(){
        return mImageView;
    }

    protected void setup(){
        mDuration = System.currentTimeMillis();
        Concat.create()
                .concat(new Concat.Function() {
                    @Override
                    public void run(Concat concat) {
                        splash(concat);
                    }
                })
                .concat(new Concat.Function() {
                    @Override
                    public void run(Concat concat) {
                        init(concat);
                    }
                })
                .concat(new Concat.Function() {
                    @Override
                    public void run(Concat concat) {
                        update(concat);
                    }
                })
                .concat(new Concat.Function() {
                    @Override
                    public void run(Concat concat) {
                        message(concat);
                    }
                })
                .concat(new Concat.Function() {
                    @Override
                    public void run(Concat concat) {
                        permission(concat);
                    }
                })
                .concat(new Concat.Function() {
                    @Override
                    public void run(Concat concat) {
                        plugin(concat);
                    }
                })
                .concat(new Concat.Function() {
                    @Override
                    public void run(Concat concat) {
                        success();
                    }
                })
                .next();
    }

    protected void splash(Concat concat){
        try {
            Context context = getBaseContext();

            RequestCreator requestCreator = Picasso
                    .with(context)
                    .load(Attribute.system().getSplashIcon());

            switch (Attribute.system().getSplashScaleType()) {
                case "centerInside":
                    requestCreator
                            .resize(UIDisplayHelper.getScreenWidth(context), UIDisplayHelper.getScreenHeight(context))
                            .centerInside();
                    break;
                case "centerCrop":
                default:
                    requestCreator
                            .resize(UIDisplayHelper.getScreenWidth(context), UIDisplayHelper.getScreenHeight(context))
                            .centerCrop();
                    break;
            }

            requestCreator.into(getImageView());
        }catch (Throwable e){
            e.printStackTrace();
        }
        concat.next();
    }

    protected void init(Concat concat) {
        showLoading();
        concat.next();
    }

    protected void update(final Concat concat) {
        if (!Attribute.system().checkUpdate()){
            concat.next();
        }else {
            if (getUpdateInfo() != null && getUpdateInfo().hasUpdate) {
                new UpdateDialog(getBaseContext())
                        .create()
                        .addOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                concat.next();
                            }
                        })
                        .show();
            }else{
                concat.next();
            }
        }
    }

    protected void message(Concat concat) {
        concat.next();
    }

    protected void permission(final Concat concat) {
        PermissionManager.runtime((Activity) getBaseContext(), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if (aBoolean.booleanValue()){
                    concat.next();
                }else{
                    ToastEx.longShow("权限授予失败!");
                }
            }
        },new ArrayList<String>());
    }

    protected void plugin(final Concat concat) {
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    PluginManager.manager().loadInternalPlugins();
                }catch (Throwable e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                concat.next();
            }
        }.execute();
    }

    protected void success() {
        long duration = System.currentTimeMillis() - mDuration;
        if (duration < Attribute.system().getSplashDuration()){
            runDelay(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            },Attribute.system().getSplashDuration() - duration);
        }else {
            dismiss();
        }
    }

    protected void showLoading(){
        try {
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
        mLoadingDialog = new IOSDialog.Tip(getBaseContext())
                .setTheme(IOSDialog.Tip.LOADING)
                .setContent("游戏初始化中")
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .create();

        mLoadingDialog.show();
    }

    protected void hideLoading(){
        try {
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    private void dismiss(){
        hideLoading();
        getDialog().dismiss();
    }

    private UpdateDialog.Info getUpdateInfo(){
        return new UpdateDialog.Info();
    }

    private NotifyDialog.Info getNotifyInfo(){
        return new NotifyDialog.Info();
    }
}
