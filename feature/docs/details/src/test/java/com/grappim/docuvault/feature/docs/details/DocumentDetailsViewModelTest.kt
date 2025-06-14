package com.grappim.docuvault.feature.docs.details

import android.content.Intent
import com.grappim.docuvault.core.navigation.destinations.DocDetailsNavRoute
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.testing.MainDispatcherRule
import com.grappim.docuvault.testing.SavedStateHandleRule
import com.grappim.docuvault.testing.getDocument
import com.grappim.docuvault.testing.getDocumentFileUI
import com.grappim.docuvault.testing.getRandomLong
import com.grappim.docuvault.utils.androidapi.intent.IntentGenerator
import com.grappim.docuvault.utils.filesapi.mappers.FileDataMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
internal class DocumentDetailsViewModelTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private val docId = getRandomLong()
    private val route = DocDetailsNavRoute(documentId = docId)

    @get:Rule
    val savedStateHandleRule = SavedStateHandleRule(route)

    private lateinit var sut: DocumentDetailsViewModel

    private val documentRepository = mockk<DocumentRepository>()
    private val fileDataMapper = mockk<FileDataMapper>()
    private val intentGenerator = mockk<IntentGenerator>()

    private val doc = getDocument()
    private val files = listOf(getDocumentFileUI(), getDocumentFileUI())

    @Before
    fun setup() {
        coEvery { documentRepository.getDocumentById(docId) } returns doc
        coEvery { fileDataMapper.toDocumentFileUiDataList(doc.files) } returns files

        sut = DocumentDetailsViewModel(
            savedStateHandle = savedStateHandleRule.savedStateHandleMock,
            documentRepository = documentRepository,
            fileDataMapper = fileDataMapper,
            intentGenerator = intentGenerator
        )
    }

    @Test
    fun `on init should call getDocument`() {
        assertEquals(doc, sut.viewState.value.document)
        assertEquals(files, sut.viewState.value.files)

        coVerify { documentRepository.getDocumentById(docId) }
        coVerify { fileDataMapper.toDocumentFileUiDataList(doc.files) }
    }

    @Test
    fun `on onFileClicked should call intentGenerator`() {
        val file = files.first()
        val intent = Intent("asd")
        every { intentGenerator.getOpenFileIntent(file.uri, file.mimeType) } returns intent

        sut.viewState.value.onFileClicked(file)

        assertEquals(intent, sut.viewState.value.openImageIntent)
        verify { intentGenerator.getOpenFileIntent(file.uri, file.mimeType) }
    }

    @Test
    fun `on updateProduct should call getDocument`() {
        sut.viewState.value.updateProduct()

        assertEquals(doc, sut.viewState.value.document)
        assertEquals(files, sut.viewState.value.files)

        coVerify(exactly = 2) { documentRepository.getDocumentById(docId) }
        coVerify(exactly = 2) { fileDataMapper.toDocumentFileUiDataList(doc.files) }
    }
}
