package com.grappim.docuvault.feature.group.repoapi

import com.grappim.docuvault.feature.group.domain.Group
import com.grappim.docuvault.feature.group.domain.GroupToCreate
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    fun getGroups(): Flow<List<Group>>

    suspend fun getGroupById(groupId: String): Group
    suspend fun createGroup(groupToCreate: GroupToCreate)
}
