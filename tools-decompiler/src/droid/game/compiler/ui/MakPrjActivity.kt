package droid.game.compiler.ui

import droid.game.compiler.Color
import droid.game.compiler.appendText
import droid.game.compiler.config.And
import droid.game.compiler.config.Inputs
import droid.game.compiler.config.Project
import droid.game.compiler.config.Properties
import droid.game.compiler.reflect.Reflect
import droid.game.compiler.task.Task
import droid.game.compiler.util.FileUtils
import droid.game.compiler.util.FileUtils.*
import droid.game.compiler.util.TextUtils
import droid.game.compiler.xml.Item
import droid.game.compiler.xml.Manifest
import droid.game.compiler.xml.Public
import com.google.gson.reflect.TypeToken
import droid.game.compiler.replacePackage
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import io.reactivex.schedulers.Schedulers
import javafx.application.Platform
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.scene.web.HTMLEditor
import javafx.util.Callback
import net.dongliu.apk.parser.ApkFile
import net.lingala.zip4j.core.ZipFile
import net.lingala.zip4j.model.FileHeader
import net.openhft.compiler.CompilerUtils
import sun.misc.BASE64Decoder
import java.io.*
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.regex.Pattern
import javax.imageio.ImageIO
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MakPrjActivity : BaseActivity() {
    @FXML
    lateinit var mListView: ListView<And.Item>

    @FXML
    lateinit var mLogCat: HTMLEditor

    private val group = ToggleGroup()

    private val configs = Collections.synchronizedMap(HashMap<String, String>())

    private val replaces = Collections.synchronizedMap(HashMap<String,String>())

    private var andItem: And.Item? = null

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
        mLogCat.appendText("I: LogCat")

        group.selectedToggleProperty().addListener({ observableValue: ObservableValue<out Toggle>, toggle: Toggle?, toggle1: Toggle? ->
            mListView.selectionModel.select((toggle1 as ToggleButton).tag as And.Item)
            refreshConfig(((toggle1 as ToggleButton).tag as And.Item).name)
            andItem = (toggle1 as ToggleButton).tag as And.Item
        })

        mListView.cellFactory = Callback {
            return@Callback object : ListCell<And.Item>() {
                override fun updateItem(item: And.Item?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (!empty) {
                        val gridPane = GridPane()
                        val label = Label(item?.name)
                        label.tooltip = Tooltip(item?.description)
                        val button = ToggleButton()
                        button.tag = item
                        button.toggleGroup = group
                        gridPane.add(label, 0, 0)
                        val l = Label()
                        gridPane.add(l, 1, 0)
                        GridPane.setHgrow(l, Priority.ALWAYS)
                        gridPane.add(button, 2, 0)
                        graphic = gridPane
                    }
                }
            }
        }

        mListView.items = FXCollections.observableArrayList(And.getItems())
    }

    private fun refreshConfig(name: String?) {
        name?.apply {
            val file = File(getProjDir(name))
            if (file.exists() && file.isDirectory) {
                val config = json<Map<String, String>>(file.absolutePath, "manifest-placeholders.json", object : TypeToken<Map<String, String>>() {}.type)
                mLogCat.htmlText = ""
                config.forEach { k, v ->
                    if (TextUtils.isEmpty(v)) {
                        try {
                            val value: String = Reflect.on(Properties::class.java).get(k)
                            mLogCat.appendText("[${k}=${value}]")
                        } catch (e: Throwable) {
                            mLogCat.appendText("[${k}=${v}]")
                        }
                    } else {
                        mLogCat.appendText("[${k}=${v}]")
                    }
                }

                val replaces = json<Map<String,String>>(file.absolutePath, "replace-android-icon-label.json", object : TypeToken<Map<String, String>>() {}.type)
                this@MakPrjActivity.replaces.putAll(replaces)
            }
        }
    }

    fun doMakeProject(actionEvent: ActionEvent) {
        andItem?.apply {
            (actionEvent.target as Button).isDisable = true

            updateConfigs()
            makeProject()

            (actionEvent.target as Button).isDisable = false
        }
    }

    /*
    fun makeProject() {
        Observable.create(ObservableOnSubscribe<String> { emitter ->
            var outputDir: String = ""
            if (TextUtils.isEmpty(andItem!!.workspace)) {
                outputDir = getDir("output", "AndroidStudio", andItem!!.name)
            } else {
                outputDir = andItem!!.workspace + File.separator
            }

            val workDir = outputDir + Inputs.getName() + File.separator
            val tempProj = Project.create(Inputs.getTempProject(), ApkFile(File(Inputs.getInput())).apkMeta.packageName)
            val project = Project.create(workDir, ApkFile(File(Inputs.getInput())).apkMeta.packageName)
            val packageName = ApkFile(File(Inputs.getInput())).apkMeta.packageName

            val task = getTask()
            task?.setLogger {
                emitter.onNext(it)
            }
            task?.input(tempProj,project)
            task?.callFirst()
            emitter.onNext("I: Copy Base Android Studio Project To Ouput Dir")
            copyDirectory(tempProj.dir, workDir)


            emitter.onNext("I: Create Project build.gradle File")
            writeFile(
                    "${getProjDir(andItem!!.name)}build-pro.gradle",
                    project.dir,
                    "build.gradle"
            )

            emitter.onNext("I: Create Project gradle.android.gamecenter.system File")
            writeFile(
                    "${getProjDir(andItem!!.name)}build-pro-gradle.android.gamecenter.system",
                    project.dir,
                    "gradle.android.gamecenter.system"
            )

            emitter.onNext("I: Create Project local.android.gamecenter.system File")
            writeFile(
                    "${getProjDir(andItem!!.name)}build-pro-local.android.gamecenter.system",
                    project.dir,
                    "local.android.gamecenter.system"
            )

            emitter.onNext("I: Create Project settings.gradle File")
            writeFile(
                    "${getProjDir(andItem!!.name)}build-pro-settings.gradle",
                    project.dir,
                    "settings.gradle"
            )

            emitter.onNext("I: Create App build.gradle File")
            writeFile(
                    "${getProjDir(andItem!!.name)}build-app.gradle",
                    project.app.dir,
                    "build.gradle"
            )

            emitter.onNext("I: Create Library build.gradle File")
            writeFile(
                    "${getProjDir(andItem!!.name)}build-lib.gradle",
                    project.lib.dir,
                    "build.gradle"
            )

            emitter.onNext("I: Create Library androidManifest.xml File")
            val libManifest = Manifest.load("${Inputs.getTempout()}${File.separator}androidManifest.xml",false)
            libManifest.setDir(getProjDir(andItem!!.name))
            libManifest.setPackageNamePrefix(when(TextUtils.isEmpty(configs["libPrefix"])){
                true ->"gl"
                else -> configs["libPrefix"]
            })
            replaceIcon(libManifest,project)
            replaceLabel(libManifest,project)
            libManifest.save("${project.lib.main}androidManifest.xml")

            emitter.onNext("I: Delete Not Necessary Class File In Jar File")
            val libs = File(project.lib.libs)
            libs?.listFiles()?.forEach { f ->
                if (f.isFile && f.name.endsWith(".jar") && !f.name.equals("unknown.jar")) {
                    val removeClasses = ArrayList<String>()
                    val classes: List<String>? = FileUtils.json<List<String>>(getProjDir(andItem!!.name), "remove-class.json", object : TypeToken<List<String>>() {}.type)
                    classes?.apply {
                        classes.replacePackage(packageName)
                        if (classes.size > 0) {
                            val zipFile = ZipFile(f)
                            for (obj in zipFile.getFileHeaders()) {
                                val header = obj as FileHeader

                                var fileName = header.fileName
                                fileName = fileName.replace("/".toRegex(), ".")
                                fileName = fileName.replace("\\\\".toRegex(), ".")

                                for (filter in classes) {
                                    if (fileName.startsWith(filter)) {
                                        removeClasses.add(header.fileName)
                                    }
                                }
                            }
                        }
                    }

                    Collections.sort<String>(removeClasses)
                    Collections.reverse(removeClasses)

                    val zipFile = ZipFile(f)
                    removeClasses.forEach {
                        var fileName = it
                        fileName = fileName.replace("/".toRegex(), ".")
                        fileName = fileName.replace("\\\\".toRegex(), ".")
                        emitter.onNext("I: Remove Class(${fileName})")
                        zipFile.removeFile(it)
                    }
                }
            }

            emitter.onNext("I: Delete Not Necessary So Files")
            val jniLibs = File(project.lib.jniLibs)
            jniLibs.listFiles().forEach {
                it.listFiles().forEach {
                    val sos: List<String>? = FileUtils.json<List<String>>(getProjDir(andItem!!.name), "remove-so.json", object : TypeToken<List<String>>() {}.type)
                    sos?.apply {
                        if(sos.contains(it.name)){
                            emitter.onNext("I: Delete ${it.name}")
                            deleteFile(it)
                        }
                    }
                }
            }

            emitter.onNext("I: Create Application Library androidManifest.xml File")
            val appManifest = Manifest.load("${Inputs.getTempout()}${File.separator}androidManifest.xml",false)
            appManifest.setDir(getProjDir(andItem!!.name))
            appManifest.setPackageNamePrefix(when(TextUtils.isEmpty(configs["appPrefix"])){
                true ->"ga"
                else -> configs["appPrefix"]
            })
            appManifest.child.clear()
            appManifest.save("${project.app.main}androidManifest.xml")

            emitter.onNext("I: Copy KeyStore File To Project")
            val sign = File(getSignDir(),configs["storeFile"])
            org.apache.commons.io.FileUtils.copyFile(sign,File(project.sig,configs["storeFile"]))

            emitter.onNext("I: Copy Public File To Project")
            try{
                org.apache.commons.io.FileUtils.copyFile(
                        File(project.lib.values,"public.xml"),
                        File(project.app.values,"public.xml")
                )
                File(project.lib.values,"public.xml").delete()
            }catch (e: Throwable){

            }

            emitter.onNext("I: Process drawable-v21 avd_hide_password.xml And  avd_show_password.xml")
            try{
                val password = HashMap<String,String>()
                val avd_hide_password = File("${project.lib.res}drawable-v21","avd_hide_password.xml")
                var avd_hide_password_content = ""
                if (avd_hide_password.exists() && avd_hide_password.isFile){
                    avd_hide_password_content = avd_hide_password.readText()
                }

                for (i in 0..2){
                    val avd_hide_password__ = File("${project.lib.res}drawable-v21","$" + "avd_hide_password__${i}.xml")
                    if (avd_hide_password__.exists() && avd_hide_password__.isFile){
                        avd_hide_password__.renameTo(File("${project.lib.res}drawable-v21","avd_hide_password__${i}.xml"))
                        avd_hide_password_content = avd_hide_password_content.replace("$" + "avd_hide_password__${i}","avd_hide_password__${i}")
                        password.put("$" + "avd_hide_password__${i}","avd_hide_password__${i}")
                        File("${project.lib.res}drawable-v21","$" + "avd_hide_password__${i}.xml").delete()
                    }
                }
                avd_hide_password.writeText(avd_hide_password_content)

                val avd_show_password = File("${project.lib.res}drawable-v21", "avd_show_password.xml")
                var avd_show_password_content = ""
                if (avd_show_password.exists() && avd_show_password.isFile){
                    avd_show_password_content = avd_show_password.readText()
                }

                for (i in 0..2){
                    val avd_show_password = File("${project.lib.res}drawable-v21","$" + "avd_show_password__${i}.xml")
                    if (avd_show_password.exists() && avd_show_password.isFile){
                        avd_show_password.renameTo(File("${project.lib.res}drawable-v21","avd_show_password__${i}.xml"))
                        avd_show_password_content = avd_show_password_content.replace("$" + "avd_show_password__${i}","avd_show_password__${i}")
                        password.put("$" + "avd_show_password__${i}","avd_show_password__${i}")
                        File("${project.lib.res}drawable-v21","$" + "avd_show_password__${i}.xml").delete()
                    }
                }
                avd_show_password.writeText(avd_show_password_content)

                val public: Public = Public.load(File(project.app.values,"public.xml").absolutePath,false)

                public.child.forEach {it as Public.PublicItem
                    if (password.containsKey(it.publicName)){
                        it.publicName = password.get(it.publicName)
                    }
                }
                File(project.app.values,"public.xml").delete()
                public.save(File(project.app.values,"public.xml").absolutePath)

            }catch (e : Throwable){

            }

            task?.callLast()

            emitter.onComplete()
        })
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .doOnNext {
                    mLogCat.appendText(it)
                }
                .doOnError {
                    mLogCat.appendText(it.message, Color.ERROR.value())
                }
                .doOnComplete {
                    var outputDir: String = ""
                    if (TextUtils.isEmpty(andItem!!.workspace)) {
                        outputDir = getDir("output", "AndroidStudio", andItem!!.name)
                    } else {
                        outputDir = andItem!!.workspace + File.separator
                    }

                    val workDir = outputDir + Inputs.getName() + File.separator
                    Runtime.getRuntime().exec("explorer ${workDir}")
                }
                .subscribe()
    }
*/
    fun makeProject() {
        Observable.create(ObservableOnSubscribe<String> { emitter ->
            var outputDir: String = ""
            if (TextUtils.isEmpty(andItem!!.workspace)) {
                outputDir = getDir("output", "AndroidStudio", andItem!!.name)
            } else {
                outputDir = andItem!!.workspace + File.separator
            }

            val workDir = outputDir + Inputs.getName() + File.separator
            val tempProj = Project.create(Inputs.getTempProject(), ApkFile(File(Inputs.getInput())).apkMeta.packageName)
            val project = Project.create(workDir, ApkFile(File(Inputs.getInput())).apkMeta.packageName)
            val packageName = ApkFile(File(Inputs.getInput())).apkMeta.packageName

            val task = getTask()
            task?.setLogger {
                emitter.onNext(it)
            }
            task?.input(tempProj,project)
            task?.callFirst()
            emitter.onNext("I: Copy Base Android Studio Project To Ouput Dir")
            copyDirectory(tempProj.dir, workDir)


            emitter.onNext("I: Create Project build.gradle File")
            writeFile(
                    "${getProjDir(andItem!!.name)}build-pro.gradle",
                    project.dir,
                    "build.gradle"
            )

            emitter.onNext("I: Create Project gradle.properties File")
            writeFile(
                    "${getProjDir(andItem!!.name)}build-pro-gradle.properties",
                    project.dir,
                    "gradle.properties"
            )

            emitter.onNext("I: Create Project local.properties File")
            writeFile(
                    "${getProjDir(andItem!!.name)}build-pro-local.properties",
                    project.dir,
                    "local.properties"
            )

            emitter.onNext("I: Create Project settings.gradle File")
            writeFile(
                    "${getProjDir(andItem!!.name)}build-pro-settings.gradle",
                    project.dir,
                    "settings.gradle"
            )

            emitter.onNext("I: Create App build.gradle File")
            writeFile(
                    "${getProjDir(andItem!!.name)}build-app.gradle",
                    project.app.dir,
                    "build.gradle"
            )

            emitter.onNext("I: Create Library build.gradle File")
            writeFile(
                    "${getProjDir(andItem!!.name)}build-lib.gradle",
                    project.lib.dir,
                    "build.gradle"
            )

            emitter.onNext("I: Create Library androidManifest.xml File")
            val libManifest = Manifest.load("${Inputs.getTempout()}${File.separator}androidManifest.xml",false)
            libManifest.setDir(getProjDir(andItem!!.name))
            libManifest.setPackageNamePrefix(when(TextUtils.isEmpty(configs["libPrefix"])){
                true ->"gl"
                else -> configs["libPrefix"]
            })
            replaceIcon(libManifest,project)
            replaceLabel(libManifest,project)
            libManifest.save("${project.lib.main}androidManifest.xml")

            emitter.onNext("I: Delete Not Necessary Class File In Jar File")
            val libs = File(project.lib.libs)
            libs?.listFiles()?.forEach { f ->
                if (f.isFile && f.name.endsWith(".jar") && !f.name.equals("unknown.jar")) {
                    val removeClasses = ArrayList<String>()
                    val classes: List<String>? = FileUtils.json<List<String>>(getProjDir(andItem!!.name), "remove-class.json", object : TypeToken<List<String>>() {}.type)
                    classes?.apply {
                        classes.replacePackage(packageName)
                        if (classes.size > 0) {
                            val zipFile = ZipFile(f)
                            for (obj in zipFile.getFileHeaders()) {
                                val header = obj as FileHeader

                                var fileName = header.fileName
                                fileName = fileName.replace("/".toRegex(), ".")
                                fileName = fileName.replace("\\\\".toRegex(), ".")

                                for (filter in classes) {
                                    if (fileName.startsWith(filter)) {
                                        removeClasses.add(header.fileName)
                                    }
                                }
                            }
                        }
                    }

                    Collections.sort<String>(removeClasses)
                    Collections.reverse(removeClasses)

                    val zipFile = ZipFile(f)
                    removeClasses.forEach {
                        var fileName = it
                        fileName = fileName.replace("/".toRegex(), ".")
                        fileName = fileName.replace("\\\\".toRegex(), ".")
                        emitter.onNext("I: Remove Class(${fileName})")
                        zipFile.removeFile(it)
                    }
                }
            }

            emitter.onNext("I: Delete Not Necessary So Files")
            val jniLibs = File(project.lib.jniLibs)
            jniLibs.listFiles().forEach {
                it.listFiles().forEach {
                    val sos: List<String>? = FileUtils.json<List<String>>(getProjDir(andItem!!.name), "remove-so.json", object : TypeToken<List<String>>() {}.type)
                    sos?.apply {
                        if(sos.contains(it.name)){
                            emitter.onNext("I: Delete ${it.name}")
                            deleteFile(it)
                        }
                    }
                }
            }

            emitter.onNext("I: Create Application Library androidManifest.xml File")
            val appManifest = Manifest.load("${Inputs.getTempout()}${File.separator}androidManifest.xml",false)
            appManifest.setDir(getProjDir(andItem!!.name))
            appManifest.setPackageNamePrefix(when(TextUtils.isEmpty(configs["appPrefix"])){
                true ->"ga"
                else -> configs["appPrefix"]
            })
            appManifest.child.clear()
            appManifest.save("${project.app.main}androidManifest.xml")

            emitter.onNext("I: Copy KeyStore File To Project")
            val sign = File(getSignDir(),configs["storeFile"])
            org.apache.commons.io.FileUtils.copyFile(sign,File(project.sig,configs["storeFile"]))

            emitter.onNext("I: Copy Public File To Project")
            try{
                org.apache.commons.io.FileUtils.copyFile(
                        File(project.lib.values,"public.xml"),
                        File(project.app.values,"public.xml")
                )
                File(project.lib.values,"public.xml").delete()
            }catch (e: Throwable){

            }

            emitter.onNext("I: Process drawable-v21 avd_hide_password.xml And  avd_show_password.xml")
            try{
                val password = HashMap<String,String>()
                val avd_hide_password = File("${project.lib.res}drawable-v21","avd_hide_password.xml")
                var avd_hide_password_content = ""
                if (avd_hide_password.exists() && avd_hide_password.isFile){
                    avd_hide_password_content = avd_hide_password.readText()
                }

                for (i in 0..2){
                    val avd_hide_password__ = File("${project.lib.res}drawable-v21","$" + "avd_hide_password__${i}.xml")
                    if (avd_hide_password__.exists() && avd_hide_password__.isFile){
                        avd_hide_password__.renameTo(File("${project.lib.res}drawable-v21","avd_hide_password__${i}.xml"))
                        avd_hide_password_content = avd_hide_password_content.replace("$" + "avd_hide_password__${i}","avd_hide_password__${i}")
                        password.put("$" + "avd_hide_password__${i}","avd_hide_password__${i}")
                        File("${project.lib.res}drawable-v21","$" + "avd_hide_password__${i}.xml").delete()
                    }
                }
                avd_hide_password.writeText(avd_hide_password_content)

                val avd_show_password = File("${project.lib.res}drawable-v21", "avd_show_password.xml")
                var avd_show_password_content = ""
                if (avd_show_password.exists() && avd_show_password.isFile){
                    avd_show_password_content = avd_show_password.readText()
                }

                for (i in 0..2){
                    val avd_show_password = File("${project.lib.res}drawable-v21","$" + "avd_show_password__${i}.xml")
                    if (avd_show_password.exists() && avd_show_password.isFile){
                        avd_show_password.renameTo(File("${project.lib.res}drawable-v21","avd_show_password__${i}.xml"))
                        avd_show_password_content = avd_show_password_content.replace("$" + "avd_show_password__${i}","avd_show_password__${i}")
                        password.put("$" + "avd_show_password__${i}","avd_show_password__${i}")
                        File("${project.lib.res}drawable-v21","$" + "avd_show_password__${i}.xml").delete()
                    }
                }
                avd_show_password.writeText(avd_show_password_content)

                val public: Public = Public.load(File(project.app.values,"public.xml").absolutePath,false)

                public.child.forEach {it as Public.PublicItem
                    if (password.containsKey(it.publicName)){
                        it.publicName = password.get(it.publicName)
                    }
                }
                File(project.app.values,"public.xml").delete()
                public.save(File(project.app.values,"public.xml").absolutePath)

            }catch (e : Throwable){

            }

            task?.callLast()

            emitter.onComplete()
        })
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .doOnNext {
                    mLogCat.appendText(it)
                }
                .doOnError {
                    mLogCat.appendText(it.message, Color.ERROR.value())
                }
                .doOnComplete {
                    var outputDir: String = ""
                    if (TextUtils.isEmpty(andItem!!.workspace)) {
                        outputDir = getDir("output", "AndroidStudio", andItem!!.name)
                    } else {
                        outputDir = andItem!!.workspace + File.separator
                    }

                    val workDir = outputDir + Inputs.getName() + File.separator
                    Runtime.getRuntime().exec("explorer ${workDir}")
                }
                .subscribe()
    }

    fun updateConfigs() {
        val htmlText = mLogCat.htmlText
        val pattern = Pattern.compile("\\[(.*?)]")
        val matcher = pattern.matcher(htmlText)
        while (matcher.find()) {
            val string = matcher.group(1)
            val split = string.split("=")
            if (split.size == 2) {
                configs.put(split[0], split[1])
            }
        }

        mLogCat.htmlText = ""
        configs.forEach { k, v ->
            if (TextUtils.isEmpty(v)) {
                try {
                    val value: String = Reflect.on(Properties::class.java).get(k)
                    mLogCat.appendText("[${k}=${value}]")
                } catch (e: Throwable) {
                    mLogCat.appendText("[${k}=${v}]")
                }
            } else {
                mLogCat.appendText("[${k}=${v}]")
            }
        }

    }

    fun copyDirectory(src: String, dest: String) {
        try {
            org.apache.commons.io.FileUtils.copyDirectory(File(src), File(dest))
        } catch (e: IOException) {
        }
    }

    fun writeFile(src: String,dest: String,name: String){
        var text = readFile(src)
        text = replaceContent(text)
        val file = File(dest,name)
        try {
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            val fw = FileWriter(file.absolutePath, false)
            fw.write(text)
            fw.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun writeFile(file: File,content: String){
        try {
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            val fw = FileWriter(file.absolutePath, false)
            fw.write(content)
            fw.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun deleteFile(file: File){
        file.delete()
    }

    fun readFile(src: String): String{
        val stream =Files.newInputStream(Paths.get(src))
        stream.buffered().reader().use{
            return it.readText()
        }
    }

    fun replaceContent(text: String): String{
        var result = text
        configs.forEach { k, v ->
            result = result.replace("$" + "{${k}}",v)
        }
        return result
    }

    fun getTask(): Task?{
        try{
            val javaCode = readFile("${getProjDir(andItem!!.name)}java${File.separator}BuildTask.java")
            val className = "droid.game.compiler.dynamic.BuildTask"
            val clazz = CompilerUtils.CACHED_COMPILER.loadFromJava(className, javaCode)
            val task = clazz.newInstance() as Task
            return task
        }catch (t: Throwable){

        }
        return null
    }

    fun replaceIcon(manifest: Manifest,project: Project){
        try{
            replaces["replace-icon"]?.apply {
                val icon = manifest.application.attr["android:icon"]
                manifest.application.attr["android:icon"] = "@drawable/com_android_rebuild_ic_launcher"
                val bytes = BASE64Decoder().decodeBuffer(replaceFirst("data:image/png;base64,",""))
                val out = FileOutputStream(File(project.lib.drawable,"com_android_rebuild_ic_launcher.png"))
                out.write(bytes)
                out.flush()
                out.close()

                val item = Item()
                item.name = "meta-data"
                item.androidName = "backup.icon"
                item.attr.put("android:name","backup.icon")
                item.attr.put("android:value",icon)
                manifest.application.child.add(0,item)
            }
        }catch (e: Throwable){
            e.printStackTrace()
        }
    }

    fun replaceLabel(manifest: Manifest,project: Project){
        try{
            replaces["replace-label"]?.apply {
                val label = manifest.application.attr["android:label"]
                manifest.application.attr["android:label"] = "@string/com_android_rebuild_app_name"
                writeFile(File(project.lib.values,"com_android_rebuild_strings.xml"),
                        "<resources>\n<string name=\"com_android_rebuild_app_name\">GAME</string>\n</resources>")

                val item = Item()
                item.name = "meta-data"
                item.androidName = "backup.label"
                item.attr.put("android:name","backup.label")
                item.attr.put("android:value",label)
                manifest.application.child.add(0,item)
            }
        }catch (e: Throwable){
            e.printStackTrace()
        }
    }
}