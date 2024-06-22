package com.grappim.docuvault.data.backupapi

import com.grappim.docuvault.feature.docs.domain.DocumentFile

interface BackupFilesRepository {
    suspend fun insertFiles(documentId: Long, files: List<DocumentFile>)

    suspend fun deleteFiles(documentId: Long, files: List<DocumentFile>)

    suspend fun deleteFilesByDocumentId(documentId: Long)

    suspend fun getAllByDocumentId(documentId: Long): List<DocumentFile>
}
