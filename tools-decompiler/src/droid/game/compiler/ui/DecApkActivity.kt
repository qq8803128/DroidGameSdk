package droid.game.compiler.ui

import droid.game.compiler.Color
import droid.game.compiler.Launcher
import droid.game.compiler.appendText
import droid.game.compiler.command.RxCommand
import droid.game.compiler.config.Bin
import droid.game.compiler.config.Ignore
import droid.game.compiler.config.Inputs
import droid.game.compiler.config.Inputs.*
import droid.game.compiler.config.Project
import droid.game.compiler.stage
import droid.game.compiler.util.FileUtils
import droid.game.compiler.util.ZipUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import io.reactivex.schedulers.Schedulers
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.web.HTMLEditor
import net.dongliu.apk.parser.ApkFile
import net.lingala.zip4j.core.ZipFile
import net.lingala.zip4j.model.FileHeader
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.util.Zip4jConstants
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*


class DecApkActivity : BaseActivity() {
    @FXML
    lateinit var mLogCat: HTMLEditor

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        super.initialize(location, resources)

        mLogCat.isVisible = false

        Platform.runLater {
            mLogCat.lookupAll(".tool-bar").forEach {
                it.isVisible = false
                it.isManaged = false
            }
            mLogCat.isVisible = true
        }
    }

    fun doDecompileApk(actionEvent: ActionEvent) {
        (actionEvent.target as Button).isDisable = true

        RxCommand.exec("java -jar ${Bin.getApktool()} d -f ${Inputs.getInput()} -o ${Inputs.getTempout()}")
                .doOnNext { mLogCat.appendText(it) }
                .doOnError { mLogCat.appendText(it.message, Color.ERROR.value()) }
                .doOnComplete { unZipDexFile() }
                .subscribe()
    }

    fun unZipDexFile() {
        val libs = getTempLibs()
        RxCommand.exec("${Bin.getEnjarify()} ${Inputs.getInput()} -o ${libs}classes.jar")
                .doOnNext { mLogCat.appendText(it) }
                .doOnError { mLogCat.appendText(it.message, Color.ERROR.value()) }
                .doOnComplete {  zipUnknownFileToJar() }
                .subscribe()
    }


    fun oldUnZipDexFile() {
        Observable.create(ObservableOnSubscribe<String> { emitter ->
            val zipFile = ZipFile(Inputs.getInput())
            val libs = getTempLibs()
            val dex = getTempDexs()

            zipFile.fileHeaders.forEach {
                val fileHeader = it as FileHeader
                if (!fileHeader.isDirectory && fileHeader.fileName.startsWith("classes") && fileHeader.fileName.endsWith(".dex")) {
                    zipFile.extractFile(fileHeader, dex)

                    RxCommand.cmd("${Bin.getDex2jar()} ${dex}${fileHeader.fileName} -f -o ${libs}${fileHeader.fileName.replace(".dex", ".jar")}")
                    emitter.onNext("I: Build ${fileHeader.fileName.replace(".dex", ".jar")} Success")
                }
            }
            emitter.onComplete()
        }).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform())
                .doOnNext {
                    mLogCat.appendText(it)
                }
                .doOnError {
                    mLogCat.appendText(it.message, Color.ERROR.value())
                }
                .doOnComplete {
                    zipUnknownFileToJar()
                }
                .subscribe()
    }


    fun zipUnknownFileToJar() {
        Observable.create(ObservableOnSubscribe<String> { emitter ->
            val zipName = getTempLibs() + "unknown.jar"
            ZipUtils.toZip(getTempUnknown(), zipName)
            val zipFile = ZipFile(zipName)
            val dir = File(getTempout())
            dir.listFiles().forEach {
                val zipParameters = ZipParameters()
                zipParameters.compressionMethod = Zip4jConstants.COMP_DEFLATE//设置压缩方法是deflate
                zipParameters.compressionLevel = Zip4jConstants.DEFLATE_LEVEL_NORMAL//设置压缩级别
                if (!Ignore.isIgnore(it)) {
                    when (it.isFile) {
                        true -> zipFile.addFile(it, zipParameters)
                        else -> zipFile.addFolder(it, zipParameters)
                    }
                }
            }
            emitter.onNext("I: Build unknown.jar Success")
            emitter.onComplete()
        }).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform())
                .doOnNext {
                    mLogCat.appendText(it)
                }
                .doOnError {
                    mLogCat.appendText(it.message, Color.ERROR.value())
                }
                .doOnComplete {
                    buildBaseProject()
                }
                .subscribe()
    }

    fun buildBaseProject(){
        Observable.create(ObservableOnSubscribe<String> {
            it.onNext("I: Make Base Android Studio Project Dir")
            val project = Project.create(Inputs.getTempProject(),ApkFile(File(Inputs.getInput())).apkMeta.packageName)

            copyDirectory(Inputs.getTempLibs(),project.lib.libs)
            copyDirectory(Inputs.getTempAssets(),project.lib.assets)
            copyDirectory(Inputs.getTempRes(),project.lib.res)
            copyDirectory(Inputs.getTempJni(),project.lib.jniLibs)

            it.onComplete()
        }).subscribeOn(Schedulers.io()).observeOn(JavaFxScheduler.platform())
                .doOnNext {
                    mLogCat.appendText(it)
                }
                .doOnError {
                    mLogCat.appendText(it.message, Color.ERROR.value())
                }
                .doOnComplete {
                    Launcher.showMakPrjActivity()
                    mLogCat.stage().close()
                }
                .subscribe()
    }

    fun copyDirectory(src: String, dest: String) {
        try {
            org.apache.commons.io.FileUtils.copyDirectory(File(src), File(dest))
        } catch (e: IOException) {
        }

    }
}