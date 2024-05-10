package com.grappim.docuvault.feature.group.repoapi.mappers

import com.grappim.docuvault.feature.group.db.model.GroupFieldEntity
import com.grappim.docuvault.feature.group.domain.GroupField

interface GroupFieldMapper {
    suspend fun toGroupFieldEntityList(list: List<GroupField>): List<GroupFieldEntity>

    suspend fun toGroupFieldEntity(groupField: GroupField): GroupFieldEntity
}
