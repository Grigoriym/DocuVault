package com.grappim.docuvault.feature.docgroup.details

import androidx.compose.ui.graphics.Color
import com.grappim.docuvault.core.navigation.destinations.GroupDetailsNavRoute
import com.grappim.docuvault.data.cleanerapi.DataCleaner
import com.grappim.docuvault.feature.docgroup.repoapi.GroupRepository
import com.grappim.docuvault.feature.docgroup.repoapi.model.Group
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.feature.docs.repoapi.models.Document
import com.grappim.docuvault.feature.docs.uiapi.DocumentListUI
import com.grappim.docuvault.testing.MainDispatcherRule
import com.grappim.docuvault.testing.SavedStateHandleRule
import com.grappim.docuvault.utils.filesapi.mappers.DocsListUIMapper
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.OffsetDateTime
import kotlin.test.assertEquals

class GroupDetailsViewModelTest {
    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private val groupId = 123L
    private val route = GroupDetailsNavRoute(groupId)

    @get:Rule
    val savedStateHandleRule = SavedStateHandleRule(route)

    private val groupRepository = mockk<GroupRepository>()
    private val documentRepository = mockk<DocumentRepository>()
    private val docsListUIMapper = mockk<DocsListUIMapper>()
    private val dataCleaner = mockk<DataCleaner>()

    private lateinit var sut: GroupDetailsViewModel

    private val group = Group(id = 123, name = "test", fields = emptyList(), color = "ffffff")
    private val doc = Document(
        documentId = 9826,
        name = "Joey Jacobs",
        createdDate = OffsetDateTime.now(),
        group = Group(
            id = 5325,
            name = "Faye Kirk",
            fields = listOf(),
            color = "hendrerit"
        ),
        files = listOf(),
        documentFolderName = "Helene Rasmussen"
    )
    private val docs = listOf(doc)

    private val uiDoc = DocumentListUI(
        id = "efficiantur",
        name = "Lynda Park",
        createdDate = "legimus",
        preview = listOf("legere"),
        groupColor = Color.Black
    )
    private val uiDocs = listOf(uiDoc)

    @Before
    fun setup() {
        coEvery { groupRepository.getGroupById(groupId = groupId) } returns group
        coEvery { documentRepository.getDocumentsByGroupId(groupId = groupId) } returns docs
        coEvery { docsListUIMapper.toDocumentListUIList(docs) } returns uiDocs

        sut = GroupDetailsViewModel(
            savedStateHandleRule.savedStateHandleMock,
            groupRepository,
            documentRepository,
            docsListUIMapper,
            dataCleaner
        )
    }

    @Test
    fun `on init should call get data`() {
        coVerify { groupRepository.getGroupById(groupId) }
        coVerify { documentRepository.getDocumentsByGroupId(groupId) }
        coVerify { docsListUIMapper.toDocumentListUIList(listOf(doc)) }

        val state = sut.viewState.value
        assertEquals(group, state.group)
        assertEquals(uiDocs, state.documents)
    }

    @Test
    fun `on showAlertDialog with value should send correct value`() {
        listOf(true, false).forEach {
            sut.viewState.value.onShowAlertDialog(it)
            assertEquals(it, sut.viewState.value.showDeletionDialog)
        }
    }

    @Test
    fun `on onDeleteClicked should set showDeletionDialog to true`() {
        assert(!sut.viewState.value.showDeletionDialog)

        sut.viewState.value.onDeleteClicked()

        assert(sut.viewState.value.showDeletionDialog)
    }

    @Test
    fun `on deletionConfirmed should delete group and update state`() {
        coEvery { dataCleaner.deleteGroup(groupId) } just Runs

        sut.viewState.value.onDeleteGroupConfirm()

        assert(sut.viewState.value.groupDeleted)
        assert(!sut.viewState.value.showDeletionDialog)

        coVerify { dataCleaner.deleteGroup(groupId) }
    }
}
