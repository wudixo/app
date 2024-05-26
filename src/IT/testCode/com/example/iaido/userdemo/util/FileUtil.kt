package com.example.iaido.userdemo.util

import java.io.File

class FileUtil {

    fun getFile(fileNameAndLocation: String): File {
        return File(javaClass.classLoader.getResource(fileNameAndLocation).file)
    }
}
