package com.grappim.docuvault.feature.docgroup.repoimpl.mappers

import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.feature.docgroup.db.model.GroupEntity
import com.grappim.docuvault.feature.docgroup.db.model.GroupWithFieldsEntity
import com.grappim.docuvault.feature.docgroup.domain.Group
import com.grappim.docuvault.feature.docgroup.domain.GroupField
import com.grappim.docuvault.feature.docgroup.domain.GroupToCreate
import com.grappim.docuvault.feature.docgroup.repoapi.mappers.GroupMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupMapperImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : GroupMapper {

    override suspend fun toGroupList(list: List<GroupWithFieldsEntity>): List<Group> =
        withContext(ioDispatcher) {
            list.map { groupFieldEntity ->
                toGroup(groupFieldEntity)
            }
        }

    override suspend fun toGroup(groupWithFieldsEntity: GroupWithFieldsEntity): Group =
        withContext(ioDispatcher) {
            Group(
                id = groupWithFieldsEntity.groupEntity.groupId,
                name = groupWithFieldsEntity.groupEntity.name,
                color = groupWithFieldsEntity.groupEntity.color,
                fields = groupWithFieldsEntity.groupFields.map { groupField ->
                    GroupField(
                        groupId = groupField.groupId,
                        name = groupField.name,
                        value = groupField.value
                    )
                }
            )
        }

    override suspend fun toGroupEntity(group: Group): GroupEntity = withContext(ioDispatcher) {
        GroupEntity(
            groupId = group.id,
            name = group.name,
            color = group.color
        )
    }

    override suspend fun toGroupEntity(groupToCreate: GroupToCreate): GroupEntity =
        withContext(ioDispatcher) {
            GroupEntity(
                name = groupToCreate.name,
                color = groupToCreate.color
            )
        }
}
