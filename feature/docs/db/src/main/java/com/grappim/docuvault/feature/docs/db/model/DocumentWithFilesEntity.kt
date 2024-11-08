package com.grappim.docuvault.feature.docs.db.model

import androidx.room.Embedded
import androidx.room.Relation

data class DocumentWithFilesEntity(
    @Embedded val documentEntity: DocumentEntity,
    @Relation(
        parentColumn = "documentId",
        entityColumn = "documentId"
    )
    val files: List<DocumentFileEntity>?
)
