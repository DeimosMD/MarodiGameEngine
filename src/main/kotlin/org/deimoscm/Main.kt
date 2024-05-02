package org.deimoscm

import com.formdev.flatlaf.FlatDarculaLaf
import org.deimoscm.gui.Home
import java.awt.Dimension
import java.awt.Font
import java.util.*
import javax.swing.JFrame
import javax.swing.UIManager
import javax.swing.filechooser.FileSystemView
import javax.swing.plaf.FontUIResource

val homeDir: String = FileSystemView.getFileSystemView().homeDirectory.path
val appdataPath = "$homeDir/.MarodiGameEngine"
val defaultLibInstallationPath: String = "$appdataPath/game_lib/latest/"
var wkDir: String = homeDir
var libInstallationPath: String = defaultLibInstallationPath
var individualProjectSettings = IndividualProjectSettings()
val engineSettings = EngineSettings()

fun main() {
    FlatDarculaLaf.setup()
    setUIFont(FontUIResource(Font(Font.MONOSPACED, Font.PLAIN, 15)))
    val frame = JFrame("Marodi Game Engine")
    frame.contentPane = Home()
    frame.minimumSize = Dimension(1200, 800)
    frame.extendedState = frame.extendedState or JFrame.MAXIMIZED_BOTH
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.isVisible = true
}

private fun setUIFont(f: FontUIResource) {
    val keys: Enumeration<*> = UIManager.getDefaults().keys()
    while (keys.hasMoreElements()) {
        val key = keys.nextElement()
        val value = UIManager.get(key)
        if (value is FontUIResource) {
            val font = Font(f.fontName, value.style, f.size)
            UIManager.put(key, FontUIResource(font))
        }
    }
}
