package com.grappim.docuvault.feature.docgroup.repoimpl

import com.grappim.docuvault.data.dbapi.DatabaseWrapper
import com.grappim.docuvault.feature.docgroup.repoapi.GroupRepository
import com.grappim.docuvault.feature.docgroup.repoapi.mappers.GroupFieldMapper
import com.grappim.docuvault.feature.docgroup.repoapi.mappers.GroupMapper
import com.grappim.docuvault.feature.docgroup.repoapi.model.Group
import com.grappim.docuvault.feature.docgroup.repoapi.model.GroupToCreate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepositoryImpl @Inject constructor(
    private val databaseWrapper: DatabaseWrapper,
    private val groupMapper: GroupMapper,
    private val groupFieldMapper: GroupFieldMapper
) : GroupRepository {

    override suspend fun updateGroup(group: Group) {
        val entity = groupMapper.toGroupEntity(group)
        databaseWrapper.groupsDao.updateGroup(entity)
    }

    override suspend fun deleteGroupById(groupId: Long) {
        databaseWrapper.groupsDao.deleteGroupById(groupId)
    }

    override fun getGroups(): Flow<List<Group>> =
        databaseWrapper.groupsDao.getGroupsFlow().map { list ->
            groupMapper.toGroupList(list)
        }

    override suspend fun getGroupById(groupId: Long): Group {
        val entity = databaseWrapper.groupsDao.getFullGroupById(groupId)
        return groupMapper.toGroup(entity)
    }

    override suspend fun createGroup(groupToCreate: GroupToCreate) {
        val entity = groupMapper.toGroupEntity(groupToCreate)
        val fields = groupFieldMapper.toGroupFieldEntityList(groupToCreate.fields)
        databaseWrapper.groupsDao.insertGroupAndFields(entity, fields)
    }
}
