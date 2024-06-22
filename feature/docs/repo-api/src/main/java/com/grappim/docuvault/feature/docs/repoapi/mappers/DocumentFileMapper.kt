package com.grappim.docuvault.feature.docs.repoapi.mappers

import com.grappim.docuvault.feature.docs.db.model.DocumentFileEntity
import com.grappim.docuvault.feature.docs.domain.CreateDocument
import com.grappim.docuvault.feature.docs.domain.DocumentFile

interface DocumentFileMapper {
    suspend fun toFileDataEntity(createDocument: CreateDocument): List<DocumentFileEntity>

    suspend fun toDocumentFileEntityList(
        documentId: Long,
        files: List<DocumentFile>
    ): List<DocumentFileEntity>

    suspend fun toDocumentFileEntity(
        documentId: Long,
        documentFile: DocumentFile
    ): DocumentFileEntity
}
