package com.grappim.docuvault.repo

import com.grappim.docuvault.data.db.dao.GroupsDao
import com.grappim.docuvault.repo.mappers.GroupMapper
import com.grappim.domain.model.group.Group
import com.grappim.domain.model.group.GroupToCreate
import com.grappim.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepositoryImpl @Inject constructor(
    private val groupsDao: GroupsDao,
    private val groupMapper: GroupMapper
) : GroupRepository {

    override fun getGroups(): Flow<List<Group>> = groupsDao.getAll().map { list ->
        list.map { groupWithFields ->
            groupMapper.toGroup(groupWithFields)
        }
    }

    override suspend fun createGroup(group: GroupToCreate) {
//        val toInsert = GroupEntity(
//            name = group.name,
//            fields = group.fields.map {
//                GroupFieldEntity(
//                    name = it.name,
//                    value = it.value
//                )
//            },
//            color = group.color
//        )
//        groupsDao.insert(toInsert)
    }
}
