package com.grappim.docuvault.feature.group.repoimpl

import com.grappim.docuvault.feature.group.db.dao.GroupsDao
import com.grappim.docuvault.feature.group.domain.Group
import com.grappim.docuvault.feature.group.domain.GroupToCreate
import com.grappim.docuvault.feature.group.repoapi.GroupRepository
import com.grappim.docuvault.feature.group.repoapi.mappers.GroupFieldMapper
import com.grappim.docuvault.feature.group.repoapi.mappers.GroupMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepositoryImpl @Inject constructor(
    private val groupsDao: GroupsDao,
    private val groupMapper: GroupMapper,
    private val groupFieldMapper: GroupFieldMapper
) : GroupRepository {

    override fun getGroups(): Flow<List<Group>> = groupsDao.getGroupsFlow().map { list ->
        groupMapper.toGroupList(list)
    }

    override suspend fun getGroupById(groupId: String): Group {
        val entity = groupsDao.getFullGroupById(groupId.toLong())
        return groupMapper.toGroup(entity)
    }

    override suspend fun createGroup(groupToCreate: GroupToCreate) {
        val entity = groupMapper.toGroupEntity(groupToCreate)
        val fields = groupFieldMapper.toGroupFieldEntityList(groupToCreate.fields)
        groupsDao.insertGroupAndFields(entity, fields)
    }
}
