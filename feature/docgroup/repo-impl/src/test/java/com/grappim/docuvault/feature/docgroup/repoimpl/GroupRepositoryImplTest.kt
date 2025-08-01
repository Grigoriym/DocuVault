package com.grappim.docuvault.feature.docgroup.repoimpl

import com.grappim.docuvault.feature.docgroup.db.dao.GroupsDao
import com.grappim.docuvault.feature.docgroup.db.model.GroupFieldEntity
import com.grappim.docuvault.feature.docgroup.domain.GroupField
import com.grappim.docuvault.feature.docgroup.repoapi.GroupRepository
import com.grappim.docuvault.feature.docgroup.repoapi.mappers.GroupFieldMapper
import com.grappim.docuvault.feature.docgroup.repoapi.mappers.GroupMapper
import com.grappim.docuvault.testing.getGroup
import com.grappim.docuvault.testing.getGroupEntity
import com.grappim.docuvault.testing.getGroupToCreate
import com.grappim.docuvault.testing.getGroupWithFieldsEntity
import com.grappim.docuvault.testing.getRandomLong
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class GroupRepositoryImplTest {

    private val groupsDao = mockk<GroupsDao>()
    private val groupMapper = mockk<GroupMapper>()
    private val groupFieldMapper = mockk<GroupFieldMapper>()

    private val sut: GroupRepository = GroupRepositoryImpl(
        groupsDao = groupsDao,
        groupMapper = groupMapper,
        groupFieldMapper = groupFieldMapper
    )

    @Test
    fun `on updateGroup should update the group`() = runTest {
        val group = getGroup()
        val groupEntity = getGroupEntity()

        coEvery { groupMapper.toGroupEntity(group) } returns groupEntity
        coEvery { groupsDao.updateGroup(groupEntity) } just Runs

        sut.updateGroup(group)
        coVerify { groupsDao.updateGroup(any()) }
    }

    @Test
    fun `on deleteGroupById deletes the group`() = runTest {
        val groupId = getRandomLong()
        coEvery { groupsDao.deleteGroupById(groupId) } just Runs

        sut.deleteGroupById(groupId)
        coVerify { groupsDao.deleteGroupById(groupId) }
    }

    @Test
    fun `on getGroupById returns the group by id`() = runTest {
        val groupId = getRandomLong()
        val groupEntity = getGroupWithFieldsEntity()
        val group = getGroup()

        coEvery { groupsDao.getFullGroupById(groupId) } returns groupEntity
        coEvery { groupMapper.toGroup(groupEntity) } returns group

        sut.getGroupById(groupId)
        coVerify { groupsDao.getFullGroupById(groupId) }
        coVerify { groupMapper.toGroup(groupEntity) }
    }

    @Test
    fun `on createGroup creates the group`() = runTest {
        val fields = listOf(
            GroupField(groupId = 4675, name = "Duane Rodriquez", value = "nominavi")
        )
        val groupToCreate = getGroupToCreate(newFields = fields)
        val groupEntity = getGroupEntity()
        val fieldsEntity = listOf(
            GroupFieldEntity(
                name = "Duane Rodriquez",
                value = "nominavi",
                groupId = 4675
            )
        )

        coEvery { groupMapper.toGroupEntity(groupToCreate) } returns groupEntity
        coEvery { groupFieldMapper.toGroupFieldEntityList(groupToCreate.fields) } returns fieldsEntity
        coEvery { groupsDao.insertGroupAndFields(groupEntity, fieldsEntity) } just Runs

        sut.createGroup(groupToCreate)

        coVerify { groupMapper.toGroupEntity(groupToCreate) }
        coVerify { groupFieldMapper.toGroupFieldEntityList(groupToCreate.fields) }
        coVerify { groupsDao.insertGroupAndFields(groupEntity, fieldsEntity) }
    }
}
