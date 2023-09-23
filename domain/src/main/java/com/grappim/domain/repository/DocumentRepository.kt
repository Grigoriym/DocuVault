package com.grappim.domain.repository

import com.grappim.domain.model.document.CreateDocument
import com.grappim.domain.model.document.Document
import com.grappim.domain.model.document.DraftDocument
import kotlinx.coroutines.flow.Flow

interface DocumentRepository {
    suspend fun addDraftDocument(): DraftDocument
    suspend fun addDocument(document: CreateDocument)
    suspend fun addDocument(document: Document)
    suspend fun addDocuments(documents: List<Document>)
    suspend fun removeDocumentById(id: Long)
    fun getAllDocumentsFlow(): Flow<List<Document>>
    suspend fun getAllDocuments(): List<Document>
    fun getDocumentById(id: Long): Flow<Document>

    suspend fun getAllUnSynced(): List<Document>
    suspend fun markAsSynced(synced: List<Document>)
}