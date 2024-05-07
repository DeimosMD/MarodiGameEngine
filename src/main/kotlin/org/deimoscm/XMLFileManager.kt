package org.deimoscm

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.io.File

interface XMLFileManager {

    fun writeToConfigFile() {
        if (file().exists()) {
            file().writeText(XmlMapper().writeValueAsString(this))
        }
    }

    fun ensureFileExists()  {
        if (!file().exists()) {
            file().parentFile.mkdirs()
            file().createNewFile()
            writeToConfigFile()
        }
    }

    fun readFromConfigFile()

    fun file(): File
}