package droid.game.plugin.sdk.delegate.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import droid.game.butterknife.ButterKnife;
import droid.game.butterknife.IContextProvider;
import droid.game.plugin.sdk.delegate.Constants;

public abstract class AdapterHelper {

    public static class ViewHolder{
        public final View itemView;
        public ViewHolder(View itemView) {
            super();
            this.itemView = itemView;
        }
    }

    public static class PluginViewHolder extends ViewHolder implements IContextProvider{

        public PluginViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public Context provideContext() {
            return Constants.getSelfContext();
        }
    }

    public abstract static class Adapter<VH extends ViewHolder> extends BaseAdapter{

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            VH vh = null;
            if (convertView == null){
                vh = onCreateViewHolder(parent);
                convertView = vh.itemView;
                convertView.setTag(vh);
            }
            vh = (VH) convertView.getTag();
            onBindViewHolder(vh,position);

            return convertView;
        }

        public abstract VH onCreateViewHolder( ViewGroup parent);

        public abstract void onBindViewHolder( VH holder, int position);
    }

}
