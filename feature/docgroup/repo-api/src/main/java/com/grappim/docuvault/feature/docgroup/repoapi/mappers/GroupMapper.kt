package com.grappim.docuvault.feature.docgroup.repoapi.mappers

import com.grappim.docuvault.feature.docgroup.db.model.GroupEntity
import com.grappim.docuvault.feature.docgroup.db.model.GroupWithFieldsEntity
import com.grappim.docuvault.feature.docgroup.domain.Group
import com.grappim.docuvault.feature.docgroup.domain.GroupToCreate

interface GroupMapper {

    suspend fun toGroupList(list: List<GroupWithFieldsEntity>): List<Group>
    suspend fun toGroup(groupWithFieldsEntity: GroupWithFieldsEntity): Group

    suspend fun toGroupEntity(group: Group): GroupEntity

    suspend fun toGroupEntity(groupToCreate: GroupToCreate): GroupEntity
}
