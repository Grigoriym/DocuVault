package com.grappim.docuvault.feature.docgroup.repoapi

import com.grappim.docuvault.feature.docgroup.repoapi.model.Group
import com.grappim.docuvault.feature.docgroup.repoapi.model.GroupToCreate
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    fun getGroups(): Flow<List<Group>>

    suspend fun getGroupById(groupId: Long): Group
    suspend fun createGroup(groupToCreate: GroupToCreate)
    suspend fun deleteGroupById(groupId: Long)

    suspend fun updateGroup(group: Group)
}
