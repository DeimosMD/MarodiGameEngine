package org.deimoscm

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.io.File

interface XMLFileManager {

    fun writeToConfigFile() {
        if (getFile().exists()) {
            getFile().writeText(XmlMapper().writeValueAsString(this))
        }
    }

    fun ensureFileExists()  {
        if (!getFile().exists()) {
            getFile().parentFile.mkdirs()
            getFile().createNewFile()
            writeToConfigFile()
        }
    }

    fun readFromConfigFile()

    fun getFile(): File
}