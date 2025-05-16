package com.grappim.docuvault.feature.docgroup.db.model

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
