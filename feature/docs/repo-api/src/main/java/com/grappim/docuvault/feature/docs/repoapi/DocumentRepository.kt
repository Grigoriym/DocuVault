package com.grappim.docuvault.feature.docs.repoapi

import com.grappim.docuvault.feature.docs.domain.CreateDocument
import com.grappim.docuvault.feature.docs.domain.Document
import com.grappim.docuvault.feature.docs.domain.DocumentFile
import com.grappim.docuvault.feature.docs.domain.DraftDocument
import kotlinx.coroutines.flow.Flow

interface DocumentRepository {
    suspend fun addDraftDocument(): DraftDocument
    suspend fun addDocument(createDocument: CreateDocument)
    suspend fun addDocument(document: Document)
    suspend fun addDocuments(documents: List<Document>)
    suspend fun removeDocumentById(id: Long)
    fun getAllDocumentsFlow(): Flow<List<Document>>
    suspend fun getDocumentById(id: Long): Document
    suspend fun deleteDocumentFile(documentId: Long, fileName: String)

    suspend fun deleteDocumentById(documentId: Long)

    suspend fun updateDocumentWithFiles(document: Document, files: List<DocumentFile>)

    suspend fun updateFilesInDocument(documentId: Long, files: List<DocumentFile>)
}
