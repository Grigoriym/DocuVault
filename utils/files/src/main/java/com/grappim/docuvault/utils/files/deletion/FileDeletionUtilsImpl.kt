package com.grappim.docuvault.utils.files.deletion

import android.content.Context
import android.net.Uri
import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.utils.files.UriParser
import com.grappim.docuvault.utils.filesapi.deletion.FileDeletionUtils
import com.grappim.docuvault.utils.filesapi.pathmanager.FolderPathManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileDeletionUtilsImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val uriParser: UriParser,
    private val folderPathManager: FolderPathManager
) : FileDeletionUtils {
    override suspend fun deleteFolder(folderName: String) {
        val file = folderPathManager.getMainFolder(folderName)
        withContext(ioDispatcher) {
            file.deleteRecursively()
        }
    }

    override suspend fun deleteFile(uriString: String): Boolean {
        val uri = uriParser.parse(uriString)
        return deleteFile(uri)
    }

    override suspend fun deleteFile(uri: Uri): Boolean {
        val deletedRows = context.contentResolver.delete(uri, null, null)
        return deletedRows > 0
    }

    override suspend fun clearMainFolder(): Boolean = withContext(ioDispatcher) {
        folderPathManager.getMainFolder().deleteRecursively()
    }
}
