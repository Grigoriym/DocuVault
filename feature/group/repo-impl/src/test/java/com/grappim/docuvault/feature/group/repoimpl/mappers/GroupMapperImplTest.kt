package com.grappim.docuvault.feature.group.repoimpl.mappers

import com.grappim.docuvault.feature.group.db.model.GroupEntity
import com.grappim.docuvault.feature.group.domain.Group
import com.grappim.docuvault.feature.group.domain.GroupField
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GroupMapperImplTest {

    private val sut = GroupMapperImpl(UnconfinedTestDispatcher())

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
