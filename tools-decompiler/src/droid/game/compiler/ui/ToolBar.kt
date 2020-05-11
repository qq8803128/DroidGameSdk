package droid.game.compiler.ui

import droid.game.compiler.Launcher
import droid.game.compiler.stage
import droid.game.compiler.util.FileUtils
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import java.net.URL
import java.util.*

class ToolBar : Initializable {
    @FXML
    var mCloseBtn: Button? = null

    @FXML
    var mMinBtn: Button? = null

    @FXML
    var mTopBtn: Button? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        if (Launcher.isAlwaysOnTop) {
            mTopBtn?.rotate = 180.0
        } else {
            mTopBtn?.rotate = 0.0
        }

        mTopBtn?.setOnAction {
            if (Launcher.isAlwaysOnTop) {
                Launcher.isAlwaysOnTop = false
                it.stage().isAlwaysOnTop = false
                mTopBtn?.rotate = 0.0
            } else {
                Launcher.isAlwaysOnTop = true
                it.stage().isAlwaysOnTop = true
                mTopBtn?.rotate = 180.0
            }
        }

        mMinBtn?.setOnAction {
            it.stage().isIconified = true
        }

        mCloseBtn?.setOnAction {
            it.stage().close()
        }
    }

    fun openWorkSpace(mouseEvent: MouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
            println(FileUtils.getWorkSpace())
            Runtime.getRuntime().exec("explorer ${FileUtils.getWorkSpace()}")
        }
    }

}
