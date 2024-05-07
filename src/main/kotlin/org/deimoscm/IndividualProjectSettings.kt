package org.deimoscm

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.io.File

class IndividualProjectSettings : XMLFileManager {
    var mainClass: String = ""
    var jarName: String = ""
    var gameClass: String = ""

    private val configFileName: String = "config.xml"
    private val file: File = File("$wkDir/$configFileName")

    override fun readFromConfigFile() {
        if (file().exists()) {
            val v: IndividualProjectSettings = XmlMapper().readValue(file().readText(), this.javaClass)
            mainClass = v.mainClass
            jarName = v.jarName
            gameClass = v.gameClass
        }
    }

    override fun file(): File {
        return file
    }
}