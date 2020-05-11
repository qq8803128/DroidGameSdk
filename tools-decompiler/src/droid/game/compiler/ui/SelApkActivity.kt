package droid.game.compiler.ui

import droid.game.compiler.*
import droid.game.compiler.config.Inputs
import droid.game.compiler.util.FileUtils.getDir
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import io.reactivex.schedulers.Schedulers
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.input.DragEvent
import javafx.scene.input.TransferMode
import javafx.stage.StageStyle
import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URL
import java.util.*

class SelApkActivity : BaseActivity() {
    @FXML
    internal var mDragFile: Label? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        super.initialize(location, resources)


        mDragFile?.run {
            val alert = Alert(Alert.AlertType.NONE)
            alert.title = "请稍等"
            alert.contentText = "初始化中\n"
            alert.dialogPane.stage().isResizable = false
            alert.dialogPane.stage().initStyle(StageStyle.UNDECORATED)
            alert.dialogPane.stage().isAlwaysOnTop = true
            alert.dialogPane.stage().icons.add("ic_launcher.png".image())
            alert.dialogPane.maxWidth = 140.0
            alert.dialogPane.minHeight = 60.0
            alert.dialogPane.children.clear()
            val parent = FXMLLoader.load<Parent>(("dlg_init_env.fxml").layout())
            alert.dialogPane.children.add(parent)
            alert.dialogPane.style = "-fx-border-width:1;-fx-border-color:#4A5459;-fx-border-radius: 5px;"
            alert.showAndWait()
        }
    }

    fun onDragFileDropped(dragEvent: DragEvent) {
        val db = dragEvent.dragboard
        var success = false
        if (db.hasFiles()) {
            val file = db.files[0]
            if (file.isFile && file.name.endsWith(".apk")) {
                copyFileToTempInput(file)
            }
            success = true
        }
        dragEvent.isDropCompleted = success
        dragEvent.consume()
    }

    fun onDragFileOver(dragEvent: DragEvent) {
        if (dragEvent.dragboard.hasFiles() && dragEvent.gestureSource !== mDragFile) {
            dragEvent.acceptTransferModes(*TransferMode.COPY_OR_MOVE)
        }
        dragEvent.consume()
    }

    fun copyFileToTempInput(file: File){
        //FileUtils.copyFile(file,File(getDir("output","base"),file.name))
        Observable.create(ObservableOnSubscribe<String> { emitter ->
            try{
                File(getDir("output","base"),file.name).delete()
            }catch (e: Throwable){

            }
            FileUtils.copyFile(file,File(getDir("output","base"),file.name))
            emitter.onNext("")
            emitter.onComplete()
        })
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .doOnNext {
                }
                .doOnError {
                }
                .doOnComplete {
                    Inputs.setInput(File(getDir("output","base"),file.name).absolutePath)
                    Launcher.showDecApkActivity()
                    mDragFile?.stage()?.close()
                }
                .subscribe()
    }
}