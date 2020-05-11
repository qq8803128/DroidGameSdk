package droid.game.gradle

import net.lingala.zip4j.core.ZipFile
import org.apache.commons.io.FileUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.Copy
import droid.game.gradle.extension.AarJarExtension

/**
 * 将aar中的classed.jar提取
 */
class AarJarPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        project.extensions.create('aarJar', AarJarExtension)

        project.configurations {
            aarJar
        }

        List<String> jarFiles = new ArrayList<>()
        List<String> aarFiles = new ArrayList<>()
        project.task('aarJar', type: Copy) {
            from(project.configurations.aarJar)
            into('build/libs')
            rename { fileName ->
                if (((String)fileName).endsWith('.aar')){
                    aarFiles.add(project.buildDir.absolutePath + File.separator + "libs" + File.separator + fileName)
                }else if (((String)fileName).endsWith('.jar')){
                    jarFiles.add(project.buildDir.absolutePath + File.separator + "libs" + File.separator + fileName)
                }
                fileName
            }
            exclude('aarjar*')
            duplicatesStrategy(DuplicatesStrategy.EXCLUDE)
        }.doLast {
            for (String jar : jarFiles){
                copyJarFile(project,jar)
            }

            for (String aar : aarFiles){
                copyAarFile(project,aar)
            }
        }.outputs.upToDateWhen { false }
    }

    void copyJarFile(Project project,String file){
        File src = new File(file)
        File dest = new File(project.file('libs' +File.separator + src.getName()).absolutePath)
        FileUtils.copyFile(src,dest)
    }

    void copyAarFile(Project project,String file){
        try {
            File src = new File(file)
            ZipFile zipFile = new ZipFile(file)
            zipFile.extractFile("classes.jar", project.file('libs' + File.separator).absolutePath)
            File dest = project.file('libs' + File.separator + "classes.jar")
            dest.renameTo(project.file('libs' +File.separator + src.getName().substring(0,src.getName().lastIndexOf('.')) + ".jar"))
        }catch(Throwable e){
            e.printStackTrace()
        }
    }
}