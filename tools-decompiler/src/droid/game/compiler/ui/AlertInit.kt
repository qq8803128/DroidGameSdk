package droid.game.compiler.ui

import droid.game.compiler.Launcher
import droid.game.compiler.stage
import droid.game.compiler.util.FileUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import io.reactivex.schedulers.Schedulers
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import net.lingala.zip4j.core.ZipFile
import java.io.File
import java.io.FileOutputStream

import java.net.URL
import java.util.ResourceBundle


class AlertInit : Initializable {
    @FXML
    internal var mCloseInit: Label? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        Observable.create(ObservableOnSubscribe<Int> { observableEmitter ->
            val file = File("${FileUtils.getJarDir()}", "init.cfg")
            if (!file.exists() || !file.isFile) {
                val `is` = Launcher.javaClass.getResourceAsStream("WorkSpace.zip")
                val workSpaceZip = File("${FileUtils.getJarDir()}", "WorkSpace.zip")

                println(workSpaceZip.absolutePath)
                val fos = FileOutputStream(workSpaceZip)

                val buffer = ByteArray(1024)
                var len = 0
                var total = 0
                while (((`is`.read(buffer)).also { len = it }) != -1) {
                    fos.write(buffer, 0, len)
                    total += len
                }
                fos.close()

                val zip = ZipFile(workSpaceZip)
                zip.extractAll(FileUtils.getJarDir())
                file.createNewFile()
            }
            observableEmitter.onNext(1)
        }).subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe {
                    mCloseInit?.stage()?.close()
                }

    }
}
