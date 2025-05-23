package com.grappim.docuvault.utils.files.creation

import android.content.Context
import android.net.Uri
import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.utils.filesapi.creation.FileCreationUtils
import com.grappim.docuvault.utils.filesapi.inforetriever.FileInfoRetriever
import com.grappim.docuvault.utils.filesapi.pathmanager.FolderPathManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileCreationUtilsImpl @Inject constructor(
    private val fileInfoRetriever: FileInfoRetriever,
    private val folderPathManager: FolderPathManager,
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FileCreationUtils {

    override suspend fun createFileLocally(uri: Uri, folderName: String): File {
        val extension = fileInfoRetriever.getFileExtension(uri)
        val localFile = File(
            folderPathManager.getMainFolder(folderName),
            fileInfoRetriever.getFileName(extension)
        )
        writeDataToFile(uri, localFile)
        Timber.d("createFileLocally, $localFile")
        return localFile
    }

    @Suppress("NestedBlockDepth")
    private suspend fun writeDataToFile(uri: Uri, newFile: File) = withContext(ioDispatcher) {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: error("can not create inputStream from $uri")

        inputStream.use { input ->
            val outputStream = FileOutputStream(newFile)
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
    }
}
