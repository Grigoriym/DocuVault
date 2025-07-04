package com.grappim.docuvault.feature.docgroup.repoimpl

import com.grappim.docuvault.feature.docgroup.db.dao.GroupsDao
import com.grappim.docuvault.feature.docgroup.domain.Group
import com.grappim.docuvault.feature.docgroup.domain.GroupToCreate
import com.grappim.docuvault.feature.docgroup.repoapi.GroupRepository
import com.grappim.docuvault.feature.docgroup.repoapi.mappers.GroupFieldMapper
import com.grappim.docuvault.feature.docgroup.repoapi.mappers.GroupMapper
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

    override suspend fun updateGroup(group: Group) {
        val entity = groupMapper.toGroupEntity(group)
        groupsDao.updateGroup(entity)
    }

    override suspend fun deleteGroupById(groupId: Long) {
        groupsDao.deleteGroupById(groupId)
    }

    override fun getGroups(): Flow<List<Group>> = groupsDao.getGroupsFlow().map { list ->
        groupMapper.toGroupList(list)
    }

    override suspend fun getGroupById(groupId: Long): Group {
        val entity = groupsDao.getFullGroupById(groupId)
        return groupMapper.toGroup(entity)
    }

    override suspend fun createGroup(groupToCreate: GroupToCreate) {
        val entity = groupMapper.toGroupEntity(groupToCreate)
        val fields = groupFieldMapper.toGroupFieldEntityList(groupToCreate.fields)
        groupsDao.insertGroupAndFields(entity, fields)
    }
}
