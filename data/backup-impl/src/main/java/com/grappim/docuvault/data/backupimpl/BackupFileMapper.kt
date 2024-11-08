package com.grappim.docuvault.data.backupimpl

import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.data.backupdb.BackupDocumentFileEntity
import com.grappim.docuvault.feature.docs.domain.DocumentFile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BackupFileMapper @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun toBackupDocumentFileDataEntity(documentId: Long, images: List<DocumentFile>) =
        withContext(ioDispatcher) {
            images.map {
                BackupDocumentFileEntity(
                    fileId = it.fileId,
                    documentId = documentId,
                    name = it.name,
                    mimeType = it.mimeType,
                    size = it.size,
                    uriString = it.uriString,
                    md5 = it.md5
                )
            }
        }

    suspend fun toDocumentFileData(list: List<BackupDocumentFileEntity>): List<DocumentFile> =
        withContext(ioDispatcher) {
            list.map {
                DocumentFile(
                    fileId = it.fileId,
                    name = it.name,
                    mimeType = it.mimeType,
                    size = it.size,
                    uriString = it.uriString,
                    md5 = it.md5
                )
            }
        }
}
