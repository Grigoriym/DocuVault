package com.grappim.docuvault.feature.docs.repoapi.mappers

import com.grappim.docuvault.feature.docs.db.model.DocumentEntity
import com.grappim.docuvault.feature.docs.db.model.FullDocumentEntity
import com.grappim.docuvault.feature.docs.domain.CreateDocument
import com.grappim.docuvault.feature.docs.domain.Document

interface DocumentMapper {

    suspend fun toDocumentList(list: List<FullDocumentEntity>): List<Document>
    suspend fun toDocument(fullDocumentEntity: FullDocumentEntity): Document

    suspend fun toDocumentEntity(createDocument: CreateDocument): DocumentEntity

    suspend fun toDocumentEntity(document: Document): DocumentEntity
}
