package com.grappim.docuvault.repo.mappers

import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.data.db.model.group.GroupEntity
import com.grappim.docuvault.data.db.model.group.GroupWithFieldsEntity
import com.grappim.domain.model.group.Group
import com.grappim.domain.model.group.GroupField
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupMapper @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun toGroup(groupWithFieldsEntity: GroupWithFieldsEntity): Group =
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

    suspend fun toGroupEntity(group: Group): GroupEntity = withContext(ioDispatcher) {
        GroupEntity(
            groupId = group.id,
            name = group.name,
            color = group.color
        )
    }
}
