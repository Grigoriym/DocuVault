package com.grappim.docuvault.repo.mappers

import com.grappim.docuvault.data.db.model.FullDocumentEntity
import com.grappim.docuvault.data.db.model.document.DocumentEntity
import com.grappim.domain.model.document.CreateDocument
import com.grappim.domain.model.document.Document

interface DocumentMapper {

    suspend fun toDocumentList(list: List<FullDocumentEntity>): List<Document>
    suspend fun toDocument(fullDocumentEntity: FullDocumentEntity): Document

    suspend fun toDocumentEntity(createDocument: CreateDocument): DocumentEntity
}
