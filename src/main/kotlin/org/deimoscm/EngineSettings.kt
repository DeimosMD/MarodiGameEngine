package org.deimoscm

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.io.File

class EngineSettings : XMLFileManager {

    // configurable options under engine settings
    var usesCustomLibInstallation = false
    var customLibInstallationPath: String = defaultLibInstallationPath
    var updateLibOnBuildTask = true

    // other saved settings
    var latestWkDir: String = wkDir
    var latestBuildTask: Int = 0

    private val file: File = File("$appdataPath/config.xml")

    init {
        ensureFileExists()
    }

    override fun readFromConfigFile() {
        if (file().exists()) {
            val v: EngineSettings = XmlMapper().readValue(file().readText(), this.javaClass)
            usesCustomLibInstallation = v.usesCustomLibInstallation
            customLibInstallationPath = v.customLibInstallationPath
            updateLibOnBuildTask = v.updateLibOnBuildTask
            latestWkDir = v.latestWkDir
            latestBuildTask = v.latestBuildTask
        }
    }

    override fun file(): File {
        return file
    }
}