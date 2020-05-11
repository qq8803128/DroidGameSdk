package droid.game.compiler

import javafx.event.ActionEvent
import javafx.scene.Node
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.web.HTMLEditor
import javafx.scene.web.WebView
import javafx.stage.Stage
import java.net.URL

inline fun String.image(): Image = Image(Launcher.javaClass.getResourceAsStream("mipmap/" + this))
inline fun String.layout(): URL = Launcher.javaClass.getResource("layout/" + this)
inline fun Node.stage(): Stage = scene.window as Stage
inline fun MouseEvent.stage(): Stage = (target as Node).stage()
inline fun ActionEvent.stage(): Stage = (target as Node).stage()
inline fun HTMLEditor.appendText(text:String?,color: Int = Color.DEBUG.value()){
    text?.apply {
        htmlText = htmlText + String.format("<div style=\"font-size:12px;color:#%06X;\">${text}</div>", color)
    }
}

enum class Color(private val c: Int){
    DEBUG(0x008000),
    ERROR(0xFF4141);

    fun value(): Int = c
}
inline fun HTMLEditor.webView(): WebView{
    return lookup("WebView") as WebView
}

class Pressed {
    var x: Double = 0.0
    var y: Double = 0.0

    fun onPressed(mouseEvent: MouseEvent): Pressed {
        x = mouseEvent.sceneX
        y = mouseEvent.sceneY
        return this
    }

    fun offsetX() = x

    fun offsetY() = y
}

inline fun List<String>.replacePackage( packageName: String?){
    val list = ArrayList<String>()
    if (packageName != null && packageName.length > 0){
        forEach {
            if (it.startsWith("$" + "packageName")){
                list.add(it)
            }
        }

        list.forEach {
            val newPackageName = it.replaceFirst("$" + "packageName","${packageName}")
            (this as ArrayList).remove(it)
            add(newPackageName)
        }
    }
}