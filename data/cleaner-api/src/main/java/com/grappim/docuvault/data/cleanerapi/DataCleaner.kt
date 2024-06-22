package com.grappim.docuvault.data.cleanerapi

import com.grappim.docuvault.feature.docs.domain.DocumentFile

interface DataCleaner {

    suspend fun deleteDocumentFile(documentId: Long, fileName: String, uriString: String): Boolean

    suspend fun deleteDocumentFiles(documentId: Long, list: List<DocumentFile>)

    suspend fun deleteDocumentData(documentId: Long, documentFolderName: String)

    suspend fun deleteTempFolder(documentFolderName: String)

    suspend fun deleteBackupFolder(documentFolderName: String)

    suspend fun clearAllData()
}
