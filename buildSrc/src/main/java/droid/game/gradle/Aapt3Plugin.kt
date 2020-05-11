package droid.game.gradle

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import droid.game.gradle.builder.FinalR3ClassBuilder
import droid.game.gradle.builder.FinalRClassBuilder
import droid.game.gradle.extension.Aapt3Extension
import groovy.util.XmlSlurper
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import java.io.File
import kotlin.reflect.KClass

class Aapt3Plugin : Plugin<Project> {
    override fun apply(project: Project) {

        project.extensions.create("aapt3", Aapt3Extension::class.java)

        project.plugins.all {
            when (it) {
                is LibraryPlugin -> applyPlugin(project, project.extensions[LibraryExtension::class].libraryVariants)
                is AppPlugin -> applyPlugin(project, project.extensions[AppExtension::class].applicationVariants)
            }
        }

    }


    private fun getPackageName(variant : BaseVariant) : String {
        val slurper = XmlSlurper(false, false)
        val list = variant.sourceSets.map { it.manifestFile }

        // According to the documentation, the earlier files in the list are meant to be overridden by the later ones.
        // So the first file in the sourceSets list should be main.
        val result = slurper.parse(list[0])
        return result.getProperty("@package").toString()
    }

    private fun applyPlugin(project: Project, variants: DomainObjectSet<out BaseVariant>) {
        variants.all { variant ->
            variant.outputs.forEach { output ->
                val processResources = output.processResources
                // TODO proper task registered as source-generating?
                processResources.doLast {
                    val packageName = getPackageName(variant)
                    val pathToR = packageName.replace('.', File.separatorChar)
                    val rFile = processResources.sourceOutputDir.resolve(pathToR).resolve("R.java")

                    val debug = processResources.name.toLowerCase().contains("debug")

                    FinalR3ClassBuilder.brewJava(rFile, processResources.sourceOutputDir,
                            packageName, "R3",debug)

                    val aapt3: Aapt3Extension?=  project.extensions.getByName("aapt3") as Aapt3Extension
                    println("aapt3 is ${aapt3}")

                    /*
                    aapt3?.apply {


                        aapt3.resourcesPackage.forEach {
                            println(it)
                            FinalRClassBuilder.brewJava(rFile, processResources.sourceOutputDir,
                                    it, "R",debug)
                        }
                    }
                    */
                }
            }
        }
    }

    private operator fun <T : Any> ExtensionContainer.get(type: KClass<T>): T {
        return getByType(type.java)!!
    }
}