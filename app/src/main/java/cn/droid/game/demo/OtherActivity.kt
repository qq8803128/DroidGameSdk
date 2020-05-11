package cn.droid.game.demo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import cn.droid.game.demo.fragment.GuiFragment
import cn.droid.game.demo.fragment.PluginFragment
import kotlinx.android.synthetic.main.act_other.*

class OtherActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.act_other)

        val titles = arrayListOf<String>("控件功能","插件功能")
        val fragments = arrayListOf<Fragment>(GuiFragment(),PluginFragment())
        val adapter: FragmentPagerAdapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int): Fragment = fragments[position]

            override fun getCount(): Int = fragments.size

            override fun getPageTitle(position: Int): CharSequence?  = titles[position]
        }

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }
}