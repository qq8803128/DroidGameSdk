package droid.game.plugin.sdk.delegate.ui.online.page;


import android.content.Context;
import android.view.View;
import droid.game.butterknife.ButterKnife;
import droid.game.butterknife.IContextProvider;
import droid.game.butterknife.Unbinder;
import droid.game.common.global.Global;
import droid.game.common.util.UIResourcesHelper;
import droid.game.plugin.sdk.delegate.Constants;
import droid.game.plugin.sdk.delegate.ui.online.dialog.Dialog;
import droid.game.x2c.X2C;

public class Page<D extends Dialog> implements IContextProvider{
    private Unbinder mBinder;
    private D mDialog;
    public final View itemView;

    public Page(View itemView,D dialog) {
        super();
        mDialog = dialog;
        this.itemView = itemView;
        mBinder = ButterKnife.bind(this,itemView);
    }

    public void unbind(){
        mBinder.unbind();
    }

    public Page bind(){
        initialize();
        return this;
    }

    protected void initialize(){

    }

    public D getDialog(){
        return mDialog;
    }

    @Override
    public Context provideContext() {
        return Constants.getSelfContext();
    }

    protected View inflate(String layoutId){
        return X2C.inflate(
                Global.getApplication(),
                Constants.getSelfContext(),
                UIResourcesHelper.getLayout(
                        Constants.getSelfContext(),
                        layoutId
                ),
                null);
    }
}