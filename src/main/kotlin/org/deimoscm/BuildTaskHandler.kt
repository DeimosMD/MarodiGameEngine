package org.deimoscm

import marodi.control.Game
import org.deimoscm.gui.Home
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import javax.swing.*

class BuildTaskHandler (
    private val selectBuildTaskDropdown: JComboBox<String>,
    private val homeFrame: Home
) : ActionListener {

    override fun actionPerformed(event: ActionEvent?) {
        SwingUtilities.invokeLater {
            homeFrame.taskRunningLabel.isVisible = true
        }
        object : SwingWorker<Void, Void>() {
            @Throws(Exception::class)
            override fun doInBackground(): Void? {
                when (selectBuildTaskDropdown.selectedIndex) {
                    0 -> {
                        runInPanelBelow()
                    }
                    1 -> {
                        runInNewWindow()
                    }
                    2 -> {
                        buildJar()
                    }
                    else -> {
                        throw RuntimeException("Build task #" + selectBuildTaskDropdown.selectedIndex + " not found.")
                    }
                }
                return null
            }
            override fun done() {
                homeFrame.taskRunningLabel.isVisible = false
            }
        }.execute()
    }


    private fun runInPanelBelow() {
        buildJar()
        val game = getGameClass().getDeclaredConstructor()?.newInstance() as Game
        homeFrame.gameGraphicsPanelSection.removeAll()
        homeFrame.gameGraphicsPanelSection.add(game.runInPanel())
        game.launch()
    }

    private fun runInNewWindow() {
        buildJar()
        val game = getGameClass().getDeclaredConstructor()?.newInstance() as Game
        game.launch()
    }

    private fun buildJar() {
        if (File("$wkDir/src").exists() && File("$wkDir/lib").exists()) {
            File("$wkDir/build").also {if (it.exists()) it.deleteRecursively()}
            File("$wkDir/build/classes").mkdirs()
            File("$wkDir/build/dependencies").mkdirs()
            File("$wkDir/build/jar").mkdirs()
            // compile classes
            val compileScriptFile = File("$wkDir/build/compile.sh").also {it.createNewFile()}
            compileScriptFile.writeText("""
                #!/bin/bash
                cd "$wkDir"
                javac -d build/classes -cp lib/*.jar${getDirArgsRecursively("$wkDir/src/java", "java", wkDir)}
                cd "$wkDir/build/dependencies"
                jar xf ../../lib/*.jar
                rm META-INF/MANIFEST.MF
            """.trimIndent())
            Runtime.getRuntime().exec("chmod +x $wkDir/build/compile.sh").waitFor()
            Runtime.getRuntime().exec("sh $wkDir/build/compile.sh").also {
                it.errorReader().lines().forEach(System.out::println)
            }
            Runtime.getRuntime().exec("rm $wkDir/build/compile.sh")
            // jar
            val jarScriptFile = File("$wkDir/build/jar.sh").also {it.createNewFile()}
            jarScriptFile.writeText("""
                #!/bin/bash
                cd "$wkDir/build/classes"
                jar cfe "$wkDir/build/jar/${individualProjectSettings.jarName}.jar" ${individualProjectSettings.mainClass} ./*
                cd "$wkDir/build/dependencies"
                jar uf "$wkDir/build/jar/${individualProjectSettings.jarName}.jar" ./*
                ${if (File("$wkDir/src/resources").listFiles()?.size != 0) """
                cd "$wkDir/src/resources"
                jar uf "$wkDir/build/jar/${individualProjectSettings.jarName}.jar" ./*
                """ else ""
            }
            """.trimIndent())
            Runtime.getRuntime().exec("chmod +x $wkDir/build/ jar.sh").waitFor()
            Runtime.getRuntime().exec("sh $wkDir/build/jar.sh").also {
                it.errorReader().lines().forEach(System.out::println)
            }
            Runtime.getRuntime().exec("rm $wkDir/build/jar.sh")
        }
    }

    fun generateProjectStructure() {
        val latestLibDir = File(libInstallationPath)
        if (latestLibDir.exists() && latestLibDir.listFiles()?.size == 1) {
            // generates directories and config file and copies lib over
            File("$wkDir/src/java").mkdirs()
            File("$wkDir/src/resources").mkdirs()
            File("$wkDir/lib/").mkdirs()
            val fileToCopy = latestLibDir.listFiles()?.get(0)!!
            val fileInLibFolder = File("$wkDir/lib/${fileToCopy.name}")
            if (fileInLibFolder.exists()) fileInLibFolder.delete()
            Files.copy(fileToCopy.toPath(), fileInLibFolder.toPath())
            individualProjectSettings.ensureFileExists()
        } else {
            // generates frame to inform that the operation failed
            val f = JFrame("Operation Failed Warning")
            f.layout = BorderLayout()
            val l = JLabel(
                "<html>" +
                        "The structure generation operation failed because there is not a release of the Marodi Game Library in $libInstallationPath"
                        + "</html>"
            )
            f.add(l, BorderLayout.NORTH)
            val b = JButton("Close")
            b.addActionListener { f.dispose() }
            f.add(b, BorderLayout.SOUTH)
            f.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
            f.setLocationRelativeTo(homeFrame)
            f.size = Dimension(400, 150)
            f.isResizable = false
            f.isVisible = true
        }
    }
}

private fun getDirArgsRecursively(searchDir: String, extension: String, workingDirPath: String): String {
    var foundExtension = false
    var args = ""
    for (f in File(searchDir).listFiles()!!) {
        if (f.extension == extension && !foundExtension) {
            foundExtension = true
            args += " ${searchDir.removePrefix("$workingDirPath/")}/*.$extension"
        } else if (f.isDirectory)
            args += getDirArgsRecursively(f.path, extension, workingDirPath)
    }
    return args
}

private fun getGameClass(): Class<*> {
    return URLClassLoader(arrayOf(URL("file://$wkDir/build/jar/${individualProjectSettings.jarName}.jar"))).loadClass(individualProjectSettings.gameClass)
}