package com.grappim.docuvault.feature.docgroup.details

import androidx.compose.ui.graphics.Color
import com.grappim.docuvault.core.navigation.GroupDetailsNavRoute
import com.grappim.docuvault.feature.docgroup.domain.Group
import com.grappim.docuvault.feature.docgroup.repoapi.GroupRepository
import com.grappim.docuvault.feature.docs.domain.Document
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.testing.MainDispatcherRule
import com.grappim.docuvault.testing.SavedStateHandleRule
import com.grappim.docuvault.utils.files.mappers.DocsListUIMapper
import com.grappim.docuvault.utils.files.models.DocumentListUI
import io.mockk.coEvery
import io.mockk.coVerify
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
//    private lateinit var savedStateHandle: SavedStateHandle

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
        preview = "legere",
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
            docsListUIMapper
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
}
