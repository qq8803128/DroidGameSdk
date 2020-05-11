package cn.droid.game.demo.fragment

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.droid.game.demo.R
import droid.game.common.dialog.IOSDialog
import droid.game.common.dialog.UIDialog
import droid.game.common.span.Span
import droid.game.common.suspen.Suspen
import droid.game.common.toast.ToastEx
import kotlinx.android.synthetic.main.fragment_gui.*

class GuiFragment : Fragment(){
    var mInputStyle = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gui,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        suspen.setOnClickListener {
            Suspen.create(activity)
                    .setTitle("温馨提示")
                    .setContent("您有一个礼包有待查收~")
                    .setPositiveButton("查看",{
                        ToastEx.shortShow("点击了查看")
                        it.hide()
                    })
                    .show()
        }

        message.setOnClickListener {
            val title = "温馨提示"
            val span = Span("是否退出游戏\n再玩30分钟即可获得\n#在线时长礼包#")
                    .add()
                    .lightText("30分钟")
                    .textColor(Color.RED,Color.RED)
                    .backgroundColor(Color.TRANSPARENT,Color.TRANSPARENT)
                    .add()
                    .lightText("#在线时长礼包#")
                    .listener { v, lightText ->
                        ToastEx.shortShow("点击了#在线时长礼包#")
                    }

            IOSDialog.Message(activity)
                    .setTitle(title)
                    .setContent(span.build())
                    .setActionContainerDivider(Color.parseColor("#75999999"),1,0,0)
                    .addAction("立即退出",true,{
                        ToastEx.shortShow("点击了立即退出")
                        it.dismiss()
                    })
                    .addSeparatorAction(Color.parseColor("#75999999"),1)
                    .addAction("再玩一下",{
                        ToastEx.shortShow("点击了再玩一下")
                        it.dismiss()
                    })
                    .create()
                    .addOnCancelListener {
                        ToastEx.shortShow("用户取消")
                    }
                    .show()
        }

        input.setOnClickListener {
            val span = Span("请输入6-20位密码")
                    .add()
                    .lightText("6-20位")
                    .textColor(Color.RED,Color.RED)
                    .backgroundColor(Color.TRANSPARENT,Color.TRANSPARENT)


            val show = mInputStyle % 2 == 0
            val inputEdit = IOSDialog.InputEdit()
            IOSDialog.Input(activity)
                    .setTitle("请输入密码")
                    .setContent(if(show){""} else{span.build()})
                    .setShowSoftInputDialog(show)
                    .setActionContainerDivider(Color.parseColor("#75999999"),1,0,0)
                    .addAction("确定",true,{
                        ToastEx.shortShow(inputEdit.text)
                        UIDialog.hideSoftInputDialog(it as Dialog?)
                        it.dismiss()
                    })
                    .setOnCreateEditTextListener {
                        it.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                        inputEdit.editText = it
                    }
                    .create()
                    .addOnCancelListener {
                        ToastEx.shortShow("用户取消")
                    }
                    .show()

            mInputStyle++

        }

        loading.setOnClickListener {
            IOSDialog.Tip(activity)
                    .setTheme(IOSDialog.Tip.LOADING)
                    .setContent("游戏初始化中")
                    .create()
                    .addOnCancelListener {
                        ToastEx.shortShow("用户取消")
                    }
                    .show()
        }

        info.setOnClickListener {
            IOSDialog.Tip(activity)
                    .setTheme(IOSDialog.Tip.INFO)
                    .setContent("游戏初始化中")
                    .create()
                    .addOnCancelListener {
                        ToastEx.shortShow("用户取消")
                    }
                    .show()
        }

        error.setOnClickListener {
            IOSDialog.Tip(activity)
                    .setTheme(IOSDialog.Tip.ERROR)
                    .setContent("游戏初始化失败")
                    .create()
                    .addOnCancelListener {
                        ToastEx.shortShow("用户取消")
                    }
                    .show()
        }

        success.setOnClickListener {
            IOSDialog.Tip(activity)
                    .setTheme(IOSDialog.Tip.SUCCESS)
                    .setContent("游戏初始化成功")
                    .create()
                    .addOnCancelListener {
                        ToastEx.shortShow("用户取消")
                    }
                    .show()
        }

        text.setOnClickListener {
            IOSDialog.Tip(activity)
                    .setTheme(IOSDialog.Tip.NONE)
                    .setContent("游戏初始化中")
                    .create()
                    .addOnCancelListener {
                        ToastEx.shortShow("用户取消")
                    }
                    .show()
        }
    }
}
