package com.grappim.docsofmine.data.db.model.document

import androidx.room.Embedded
import androidx.room.Relation

data class DocumentWithFilesEntity(
    @Embedded val documentEntity: DocumentEntity,
    @Relation(
        parentColumn = "documentId",
        entityColumn = "documentId"
    )
    val files: List<DocumentFileDataEntity>?
)
