package com.grappim.domain.repository

import com.grappim.domain.CreateDocument
import com.grappim.domain.Document
import com.grappim.domain.DraftDocument
import kotlinx.coroutines.flow.Flow

interface DocumentRepository {
    suspend fun addDraftDocument(): DraftDocument
    suspend fun addDocument(document: CreateDocument)
    suspend fun removeDocumentById(id: Long)
    fun getAllDocumentsFlow(): Flow<List<Document>>
    suspend fun getAllDocuments(): List<Document>
    fun getDocumentById(id: Long): Flow<Document>

    suspend fun getAllUnSynced(): List<Document>
    suspend fun markAsSynced()
}