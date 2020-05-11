package droid.game.gradle

import net.lingala.zip4j.core.ZipFile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.Copy
import droid.game.gradle.extension.JarJarExtension


class JarJarPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create('jarJar', JarJarExtension)

        project.configurations {
            classRename
        }

        project.task('renamePackage', type: Copy) {
            group('rename')
            from(project.configurations.classRename)
            println(project.configurations.classRename)
            into('build/libs')
            rename { fileName ->
                fileName + '.original'
            }
            exclude('jarjar*')
            duplicatesStrategy(DuplicatesStrategy.EXCLUDE)
        }.doLast {
            releaseJarJarFile(project)

            def rules = project.jarJar.rules

            rules.keySet().each { key ->
                try {
                    if (!project.file("build/libs/${key}").exists()) {
                        List<String> ruleParts = rules[key]
                        wirteRuleFile(project, key + ".rule.txt", ruleParts)
                    }

                    project.exec {
                        File file = project.file("libs/${key}")
                        if (file.exists() && file.isFile()) {
                            file.delete()
                        }
                        String k = key
                        if (!project.file("build/libs/${key}.original").exists() && project.file("build/libs/${k.replace(".jar", ".aar")}.original").exists()) {
                            copyAarFile(project.file("build/libs/${k.replace(".jar", ".aar")}.original").absolutePath)
                        }
                        commandLine 'cmd', '/c', 'java', '-jar',
                                project.file(project.jarJar.jarJarDependency).absolutePath,
                                'process',
                                project.file("build/libs/${key}.rule.txt").absolutePath,
                                project.file("build/libs/${key}.original").absolutePath,
                                project.file("libs/${key}").absolutePath
                    }

                    project.file("build/libs/${key}.rule.txt").delete()
                    project.file("build/libs/${key}.original").delete()
                }catch(Throwable e){
                    e.printStackTrace()
                }
            }
        }.outputs.upToDateWhen { false }
    }

    void wirteRuleFile(Project project, String fileName,List<String> rules){
        if (project.file("build/libs/${fileName}").exists() && project.file("build/libs/${fileName}").isFile()){
            project.file("build/libs/${fileName}").deleteOnExit()
        }
        project.file("build/libs/${fileName}").withPrintWriter { out ->
            rules.each {
                out.print(it)
                out.print('\r\n')
            }
        }
    }

    void copyAarFile(String file){
        try {
            println(file)
            File src = new File(file)
            ZipFile zipFile = new ZipFile(file)
            zipFile.extractFile("classes.jar", src.parentFile.absolutePath)
            File dest = new File(src.parentFile,"classes.jar")
            dest.renameTo(new File(src.parentFile,src.name.replace(".aar.original",".jar.original")))
            src.delete()
        }catch(Throwable e){
            e.printStackTrace()
        }
    }

    private static void writeToLocal(String destination, InputStream input)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        downloadFile.close()
        input.close()
    }

    String releaseJarJarFile(Project project){
        if (project.jarJar.jarJarDependency.length() <= 0){
            project.jarJar.jarJarDependency = project.buildDir.absolutePath + File.separator + "jarJar/jarjar-1.7.2.jar"

            File dir = new File(project.buildDir.absolutePath + File.separator + "jarJar/")
            if (!dir.exists() || !dir.isDirectory()){
                dir.mkdirs()
            }

            File file = new File(project.buildDir.absolutePath + File.separator + "jarJar/jarjar-1.7.2.jar")
            if (file.exists() && file.isFile()){
                return
            }

            getClass().getResource('jarjar-1.7.2.jar').withInputStream { ris ->
                file.withOutputStream { fos ->
                    fos << ris
                }
            }
        }
    }
}