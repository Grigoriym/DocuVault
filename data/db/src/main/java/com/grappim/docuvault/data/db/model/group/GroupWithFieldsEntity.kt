package com.grappim.docuvault.data.db.model.group

import androidx.room.Embedded
import androidx.room.Relation

data class GroupWithFieldsEntity(
    @Embedded val groupEntity: GroupEntity,
    @Relation(
        parentColumn = "groupId",
        entityColumn = "groupId"
    )
    val groupFields: List<GroupFieldEntity>
)
