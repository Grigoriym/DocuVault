package com.grappim.docuvault.utils.files.pathmanager

import java.io.File

interface FolderPathManager {
    fun getMainFolder(productFolder: String = ""): File

    fun getTempFolderName(folder: String): String

    fun getBackupFolderName(folder: String): String
}
