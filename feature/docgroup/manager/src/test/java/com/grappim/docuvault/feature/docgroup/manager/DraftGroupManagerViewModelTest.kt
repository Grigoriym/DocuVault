package com.grappim.docuvault.feature.docgroup.manager

import androidx.compose.ui.graphics.Color
import app.cash.turbine.test
import com.grappim.docuvault.core.navigation.GroupManagerNavRoute
import com.grappim.docuvault.feature.docgroup.domain.GroupToCreate
import com.grappim.docuvault.feature.docgroup.repoapi.GroupRepository
import com.grappim.docuvault.testing.MainDispatcherRule
import com.grappim.docuvault.testing.SavedStateHandleRule
import com.grappim.docuvault.uikit.theme.Dom_Aero
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

class DraftGroupManagerViewModelTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var sut: GroupManagerViewModel

    private val groupRepository = mockk<GroupRepository>()
    private val colorUtils = mockk<ColorUtils>()

    private val route = GroupManagerNavRoute()

    @get:Rule
    val savedStateHandleRule = SavedStateHandleRule(route)

    @Before
    fun setup() {
        sut = GroupManagerViewModel(
            groupRepository = groupRepository,
            colorUtils = colorUtils,
            savedStateHandle = savedStateHandleRule.savedStateHandleMock
        )
    }

    @Test
    fun `on setName should set a new name`() {
        assert(sut.viewState.value.name.isEmpty())
        val newName = "New Name"
        sut.viewState.value.setName(newName)
        assert(sut.viewState.value.name == newName)
    }

    @Test
    fun `on setColor should set a new color`() {
        assert(sut.viewState.value.color == Dom_Aero)
        val newColor = Color.Cyan
        sut.viewState.value.setColor(newColor)
        assert(sut.viewState.value.color == newColor)
    }

    @Test
    fun `on onGroupDone should create a new group`() {
        val color = Color.Cyan
        val colorString = "0xFF00FFFF"
        val name = "test name"
        val group = GroupToCreate(
            name = name,
            fields = emptyList(),
            color = colorString
        )

        sut.viewState.value.setColor(color)
        sut.viewState.value.setName(name)

        every { colorUtils.toHexString(color) } returns colorString
        coEvery { groupRepository.createGroup(group) } just Runs

        assert(!sut.viewState.value.groupSaved)

        sut.viewState.value.onGroupDone()

        assert(sut.viewState.value.groupSaved)

        verify { colorUtils.toHexString(color) }
        coVerify { groupRepository.createGroup(group) }
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
