package com.grappim.docuvault.feature.group.repoapi.mappers

import com.grappim.docuvault.feature.group.db.model.GroupEntity
import com.grappim.docuvault.feature.group.db.model.GroupWithFieldsEntity
import com.grappim.docuvault.feature.group.domain.Group
import com.grappim.docuvault.feature.group.domain.GroupToCreate

interface GroupMapper {

    suspend fun toGroupList(list: List<GroupWithFieldsEntity>): List<Group>
    suspend fun toGroup(groupWithFieldsEntity: GroupWithFieldsEntity): Group

    suspend fun toGroupEntity(group: Group): GroupEntity

    suspend fun toGroupEntity(groupToCreate: GroupToCreate): GroupEntity
}
