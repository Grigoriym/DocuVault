package com.grappim.docuvault.feature.docgroup.repoapi

import com.grappim.docuvault.feature.docgroup.domain.Group
import com.grappim.docuvault.feature.docgroup.domain.GroupToCreate
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    fun getGroups(): Flow<List<Group>>

    suspend fun getGroupById(groupId: Long): Group
    suspend fun createGroup(groupToCreate: GroupToCreate)
}
