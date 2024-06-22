package com.grappim.docuvault.data.cleanerimpl

import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.data.cleanerapi.DataCleaner
import com.grappim.docuvault.data.dbapi.DatabaseWrapper
import com.grappim.docuvault.feature.docs.domain.DocumentFile
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.utils.files.deletion.FileDeletionUtils
import com.grappim.docuvault.utils.files.pathmanager.FolderPathManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataCleanerImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val fileDeletionUtils: FileDeletionUtils,
    private val documentRepository: DocumentRepository,
    private val folderPathManager: FolderPathManager,
    private val databaseWrapper: DatabaseWrapper
) : DataCleaner {
    override suspend fun deleteDocumentFile(
        documentId: Long,
        fileName: String,
        uriString: String
    ): Boolean = withContext(ioDispatcher) {
        if (fileDeletionUtils.deleteFile(uriString)) {
            documentRepository.deleteDocumentFile(documentId, fileName)
            return@withContext true
        }
        false
    }

    override suspend fun deleteDocumentFiles(documentId: Long, list: List<DocumentFile>) =
        withContext(ioDispatcher) {
            list.forEach {
                deleteDocumentFile(
                    documentId = documentId,
                    fileName = it.name,
                    uriString = it.uriString
                )
            }
        }

    override suspend fun deleteDocumentData(documentId: Long, documentFolderName: String) =
        withContext(ioDispatcher) {
            fileDeletionUtils.deleteFolder(documentFolderName)
            documentRepository.deleteDocumentById(documentId)
        }

    override suspend fun deleteTempFolder(documentFolderName: String) = withContext(ioDispatcher) {
        fileDeletionUtils.deleteFolder(folderPathManager.getTempFolderName(documentFolderName))
    }

    override suspend fun deleteBackupFolder(documentFolderName: String) {
        fileDeletionUtils.deleteFolder(folderPathManager.getBackupFolderName(documentFolderName))
    }

    override suspend fun clearAllData() {
        databaseWrapper.clearAllTables()
        clearFileSystemData()
    }

    private suspend fun clearFileSystemData() = fileDeletionUtils.clearMainFolder()
}
