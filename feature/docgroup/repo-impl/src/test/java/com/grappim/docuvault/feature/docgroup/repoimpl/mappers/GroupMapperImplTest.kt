package com.grappim.docuvault.feature.docgroup.repoimpl.mappers

import com.grappim.docuvault.feature.docgroup.db.model.GroupEntity
import com.grappim.docuvault.feature.docgroup.db.model.GroupWithFieldsEntity
import com.grappim.docuvault.feature.docgroup.repoapi.model.Group
import com.grappim.docuvault.feature.docgroup.repoapi.model.GroupField
import com.grappim.docuvault.testing.getGroup
import com.grappim.docuvault.testing.getGroupToCreate
import com.grappim.docuvault.testing.getGroupWithFieldsEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GroupMapperImplTest {

    private val sut = GroupMapperImpl(UnconfinedTestDispatcher())

    @Test
    fun `to toGroupList should return correct GroupList`() = runTest {
        val entity = listOf(
            GroupWithFieldsEntity(
                groupEntity = GroupEntity(
                    groupId = 7961,
                    name = "Florence Conley",
                    color = "persecuti"
                ),
                groupFields = listOf()
            )
        )
        val actual = sut.toGroupList(entity)

        val expected = listOf(
            Group(
                id = 7961,
                name = "Florence Conley",
                color = "persecuti",
                fields = emptyList()
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `on toGroup should return correct Group`() = runTest {
        val entity = getGroupWithFieldsEntity()
        val actual = sut.toGroup(entity)
        val fields = entity.groupFields.map {
            GroupField(
                groupId = it.groupId,
                name = it.name,
                value = it.value
            )
        }

        val expected = Group(
            id = entity.groupEntity.groupId,
            name = entity.groupEntity.name,
            fields = fields,
            color = entity.groupEntity.color
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `toGroupEntity with Group should return correct GroupEntity`() = runTest {
        val group = getGroup()

        val actual = sut.toGroupEntity(group)
        val expected = GroupEntity(
            groupId = group.id,
            name = group.name,
            color = group.color
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `toGroupEntity with GroupToCreate should return correct GroupEntity`() = runTest {
        val groupToCreate = getGroupToCreate()

        val actual = sut.toGroupEntity(groupToCreate)
        val expected = GroupEntity(
            name = groupToCreate.name,
            color = groupToCreate.color
        )

        assertEquals(expected, actual)
    }
}
