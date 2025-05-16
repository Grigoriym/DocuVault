package com.grappim.docuvault.feature.docs.db.model

import androidx.room.Embedded
import androidx.room.Relation
import com.grappim.docuvault.feature.docgroup.db.model.GroupEntity
import com.grappim.docuvault.feature.docgroup.db.model.GroupFieldEntity

data class FullDocumentEntity(
    @Embedded val documentEntity: DocumentEntity,
    @Relation(
        parentColumn = "documentId",
        entityColumn = "documentId"
    )
    val documentFiles: List<DocumentFileEntity>,
    @Relation(
        parentColumn = "groupId",
        entityColumn = "groupId"
    )
    val group: GroupEntity,
    @Relation(
        parentColumn = "groupId",
        entityColumn = "groupId"
    )
    val groupFields: List<GroupFieldEntity>
)
