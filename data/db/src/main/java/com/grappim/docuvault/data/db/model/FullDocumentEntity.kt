package com.grappim.docuvault.data.db.model

import androidx.room.Embedded
import androidx.room.Relation
import com.grappim.docuvault.data.db.model.document.DocumentEntity
import com.grappim.docuvault.data.db.model.document.DocumentFileEntity
import com.grappim.docuvault.data.db.model.group.GroupEntity
import com.grappim.docuvault.data.db.model.group.GroupFieldEntity

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
