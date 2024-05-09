package com.grappim.docuvault.repo.mappers

import com.grappim.docuvault.data.db.model.document.DocumentFileEntity
import com.grappim.domain.model.document.CreateDocument

interface DocumentFileMapper {
    suspend fun toFileDataEntity(createDocument: CreateDocument): List<DocumentFileEntity>
}
