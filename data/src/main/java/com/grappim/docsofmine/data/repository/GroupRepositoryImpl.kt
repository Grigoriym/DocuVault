package com.grappim.docsofmine.data.repository

import com.grappim.docsofmine.data.db.dao.GroupsDao
import com.grappim.docsofmine.data.db.model.GroupEntity
import com.grappim.docsofmine.data.db.model.GroupFieldEntity
import com.grappim.domain.Group
import com.grappim.domain.GroupField
import com.grappim.domain.GroupToCreate
import com.grappim.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepositoryImpl @Inject constructor(
    private val groupsDao: GroupsDao
) : GroupRepository {

    override fun getGroups(): Flow<List<Group>> =
        groupsDao.getAll()
            .map {
                it.map { entity ->
                    Group(
                        id = entity.id,
                        name = entity.name,
                        fields = entity.fields.map { categoryFieldEntity ->
                            GroupField(
                                name = categoryFieldEntity.name,
                                value = categoryFieldEntity.value
                            )
                        },
                        color = entity.color
                    )
                }
            }

    override suspend fun createGroup(group: GroupToCreate) {
        val toInsert = GroupEntity(
            name = group.name,
            fields = group.fields.map {
                GroupFieldEntity(
                    name = it.name,
                    value = it.value
                )
            },
            color = group.color
        )
        groupsDao.insert(toInsert)
    }

}