package com.grappim.docuvault.feature.docgroup.repoapi.mappers

import com.grappim.docuvault.feature.docgroup.db.model.GroupFieldEntity
import com.grappim.docuvault.feature.docgroup.domain.GroupField

interface GroupFieldMapper {
    suspend fun toGroupFieldEntityList(list: List<GroupField>): List<GroupFieldEntity>

    suspend fun toGroupFieldEntity(groupField: GroupField): GroupFieldEntity
}
