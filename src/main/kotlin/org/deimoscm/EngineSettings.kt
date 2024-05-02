package org.deimoscm

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.io.File

class EngineSettings : XMLFileManager {

    // configurable options
    var usesCustomLibInstallation = false
    var customLibInstallationPath: String = defaultLibInstallationPath
    var updateLibOnBuildTask = true

    // other saved info
    var latestWkDir: String = wkDir

    private val file: File = File("$appdataPath/config.xml")

    init {
        ensureFileExists()
    }

    override fun readFromConfigFile() {
        if (file.exists()) {
            val v: EngineSettings = XmlMapper().readValue(file.readText(), this.javaClass)
            usesCustomLibInstallation = v.usesCustomLibInstallation
            customLibInstallationPath = v.customLibInstallationPath
            updateLibOnBuildTask = v.updateLibOnBuildTask
            latestWkDir = v.latestWkDir
        }
    }

    override fun getFile(): File {
        return file
    }
}