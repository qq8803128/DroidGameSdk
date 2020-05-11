package cn.droid.game.demo.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.droid.game.demo.R
import droid.game.plugin.manager.LoadedApk
import droid.game.plugin.manager.PluginManager
import kotlinx.android.synthetic.main.fragment_plugin.*
import kotlinx.android.synthetic.main.item_plugin_info.view.*

class PluginFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_plugin,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(activity)

        recyclerView.adapter = object : Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val inflater = LayoutInflater.from(activity)
                return object : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_plugin_info,null)){}
            }

            override fun getItemCount(): Int {
                return PluginManager.manager().allLoadedApks.size
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val loadedApk = PluginManager.manager().allLoadedApks[position]
                holder.itemView.imageView.setImageDrawable(loadedApk.icon)
                holder.itemView.pluginName.setText(loadedApk.label + "(" + loadedApk.packageName + ")")
                holder.itemView.pluginInfo.setText("versionName:" + loadedApk.packageInfo.versionName )
                holder.itemView.pluginInfo1.setText( "versionCode:" + loadedApk.packageInfo.versionCode)
            }
        }
    }
}
