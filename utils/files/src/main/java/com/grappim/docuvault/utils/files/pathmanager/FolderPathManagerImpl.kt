package com.grappim.docuvault.utils.files.pathmanager

import android.content.Context
import com.grappim.docuvault.utils.filesapi.pathmanager.FolderPathManager
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderPathManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FolderPathManager {

    /**
     * /data/data/com.grappim.docuvault.debug/files/docufiles/1_2024-01-23_20-04-41/
     */
    override fun getMainFolder(productFolder: String): File {
        val folder = File(context.filesDir, "docufiles/$productFolder")
        if (folder.exists().not()) {
            folder.mkdirs()
        }
        Timber.d("getMainFolder: $folder")
        return folder
    }

    /**
     * /data/data/com.grappim.docuvault.debug/files/docufiles/1_2024-01-23_20-04-41_temp/
     */
    override fun getTempFolderName(folder: String): String = "${folder}_temp"

    /**
     * /data/data/com.grappim.docuvault.debug/files/docufiles/1_2024-01-23_20-04-41_backup/
     */
    override fun getBackupFolderName(folder: String): String = "${folder}_backup"
}
