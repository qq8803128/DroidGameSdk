package droid.game.compiler

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.stage.StageStyle

class Launcher : Application() {
    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        showSelApkActivity()
    }


    companion object {

        var isAlwaysOnTop = true

        private fun create(layout: String){
            val primaryStage = Stage()
            primaryStage.isResizable = false
            primaryStage.initStyle(StageStyle.UNDECORATED)
            primaryStage.isAlwaysOnTop = isAlwaysOnTop
            primaryStage.title = "APK反编译工具"
            primaryStage.icons.add("ic_launcher.png".image())
            primaryStage.scene = Scene(FXMLLoader.load<Parent>((layout + ".fxml").layout()))
            primaryStage.show()
        }

        fun showSelApkActivity() {
            create("act_sel_apk")
        }

        fun showDecApkActivity() {
            create("act_dec_apk")
        }

        fun showMakPrjActivity() {
            create("act_mak_prj")
        }

    }

}

fun main(args: Array<String>) {
    Application.launch(Launcher::class.java, *args)
}
