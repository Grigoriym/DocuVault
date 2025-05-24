package com.grappim.docuvault.feature.docgroup.list

import androidx.compose.ui.graphics.Color
import com.grappim.docuvault.feature.docgroup.domain.Group
import com.grappim.docuvault.feature.docgroup.repoapi.GroupRepository
import com.grappim.docuvault.feature.docgroup.uiapi.GroupUI
import com.grappim.docuvault.feature.docgroup.uiapi.GroupUIMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class GroupListViewModelTest {

    private lateinit var sut: GroupListViewModel

    private val groupRepo = mockk<GroupRepository>()
    private val groupUIMapper = mockk<GroupUIMapper>()

    private val groups = listOf(
        Group(
            id = 4735,
            name = "Darwin Nieves",
            fields = listOf(),
            color = "honestatis"
        ),
        Group(
            id = 9968,
            name = "Tricia Christian",
            fields = listOf(),
            color = "euismod"
        )
    )

    private val groupUiList = listOf(
        GroupUI(
            id = 4735,
            name = "Darwin Nieves",
            fields = listOf(),
            color = Color.Black,
            colorString = ""
        ),
        GroupUI(
            id = 9968,
            name = "Tricia Christian",
            fields = listOf(),
            color = Color.Black,
            colorString = ""
        )
    )

    @Before
    fun setup() {
        every { groupRepo.getGroups() } returns flowOf(groups)
        coEvery { groupUIMapper.toGroupUIList(groups) } returns groupUiList

        sut = GroupListViewModel(groupRepository = groupRepo, groupUIMapper = groupUIMapper)
    }

    @Test
    fun `on init should run fetchGroups`() = runTest {
        verify { groupRepo.getGroups() }
        coVerify { groupUIMapper.toGroupUIList(groups) }
    }
}
