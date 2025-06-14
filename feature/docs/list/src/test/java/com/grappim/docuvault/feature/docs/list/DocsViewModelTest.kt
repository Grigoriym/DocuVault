package com.grappim.docuvault.feature.docs.list

import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.testing.MainDispatcherRule
import com.grappim.docuvault.testing.getDocument
import com.grappim.docuvault.testing.getDocumentListUi
import com.grappim.docuvault.utils.filesapi.mappers.DocsListUIMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class DocsViewModelTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var sut: DocsViewModel

    private val documentRepository = mockk<DocumentRepository>()
    private val docsListUiMapper = mockk<DocsListUIMapper>()

    private val docs = listOf(getDocument(), getDocument())
    private val uiDocs = listOf(getDocumentListUi(), getDocumentListUi())

    @Before
    fun setup() {
        coEvery { documentRepository.getAllDocumentsFlow() } returns flowOf(docs)
        coEvery { docsListUiMapper.toDocumentListUIList(docs) } returns uiDocs

        sut = DocsViewModel(documentRepository, docsListUiMapper)
    }

    @Test
    fun `on init get all documents`() {
        coVerify { documentRepository.getAllDocumentsFlow() }
        coVerify { docsListUiMapper.toDocumentListUIList(docs) }

        assertEquals(uiDocs, sut.viewState.value.documents)
    }
}
