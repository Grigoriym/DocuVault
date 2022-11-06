package com.grappim.domain.repository

import com.grappim.domain.Group
import com.grappim.domain.GroupToCreate
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    fun getGroups(): Flow<List<Group>>
    suspend fun createGroup(group: GroupToCreate)
}