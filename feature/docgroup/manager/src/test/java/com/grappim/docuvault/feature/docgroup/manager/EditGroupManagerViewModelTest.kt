package com.grappim.docuvault.feature.docgroup.manager

import androidx.compose.ui.graphics.Color
import app.cash.turbine.test
import com.grappim.docuvault.core.navigation.destinations.GroupManagerNavRoute
import com.grappim.docuvault.feature.docgroup.domain.Group
import com.grappim.docuvault.feature.docgroup.repoapi.GroupRepository
import com.grappim.docuvault.testing.MainDispatcherRule
import com.grappim.docuvault.testing.SavedStateHandleRule
import com.grappim.docuvault.uikit.utils.ColorUtils
import com.grappim.docuvault.utils.ui.NativeText
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertIs

class EditGroupManagerViewModelTest {
    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var sut: GroupManagerViewModel

    private val groupRepository = mockk<GroupRepository>()
    private val colorUtils = mockk<ColorUtils>()

    private val groupId: Long = 1234L
    private val route = GroupManagerNavRoute(groupId)

    private val colorString = "0xFF444444"
    private val color = Color.DarkGray

    @get:Rule
    val savedStateHandleRule = SavedStateHandleRule(route)

    private val group = Group(
        id = 9291,
        name = "Sammie Bowen",
        fields = listOf(),
        color = colorString
    )

    @Before
    fun setup() {
        coEvery { groupRepository.getGroupById(groupId) } returns group
        every { colorUtils.toComposeColor(colorString) } returns color
        sut = GroupManagerViewModel(
            groupRepository = groupRepository,
            colorUtils = colorUtils,
            savedStateHandle = savedStateHandleRule.savedStateHandleMock
        )
    }

    @Test
    fun `on init should prepare the edit group`() {
        assert(sut.viewState.value.name == group.name)
        assert(sut.viewState.value.color == color)

        coVerify { groupRepository.getGroupById(groupId) }
        verify { colorUtils.toComposeColor(colorString) }
    }

    @Test
    fun `on setName should set a new name`() {
        val newName = "New Name"
        sut.viewState.value.setName(newName)
        assert(sut.viewState.value.name == newName)
    }

    @Test
    fun `on setColor should set a new color`() {
        val newColor = Color.Cyan
        sut.viewState.value.setColor(newColor)
        assert(sut.viewState.value.color == newColor)
    }

    @Test
    fun `on onGroupDone should update the group`() {
        val newColor = Color.Cyan
        val newColorString = "0xFF00FFFF"
        val name = "test name"
        val updatedGroup = Group(
            id = groupId,
            name = name,
            fields = emptyList(),
            color = newColorString
        )

        sut.viewState.value.setColor(newColor)
        sut.viewState.value.setName(name)

        every { colorUtils.toHexString(newColor) } returns newColorString
        coEvery { groupRepository.updateGroup(updatedGroup) } just Runs

        assert(!sut.viewState.value.groupSaved)

        sut.viewState.value.onGroupDone()

        assert(sut.viewState.value.groupSaved)

        verify { colorUtils.toHexString(newColor) }
        coVerify { groupRepository.updateGroup(updatedGroup) }
    }

    @Test
    fun `on onGroupDone with empty name should send a snackbar message`() = runTest {
        val name = ""

        sut.snackBarMessage.test {
            sut.viewState.value.setName(name)

            sut.viewState.value.onGroupDone()
            assertIs<NativeText.Resource>(awaitItem())

            assert(!sut.viewState.value.groupSaved)

            verify(exactly = 0) { colorUtils.toHexString(any()) }
            coVerify(exactly = 0) { groupRepository.createGroup(any()) }
        }
    }
}
