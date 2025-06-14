package com.grappim.docuvault.feature.docs.manager

import app.cash.turbine.test
import com.grappim.docuvault.core.navigation.destinations.DocManagerNavRoute
import com.grappim.docuvault.data.cleanerapi.DataCleaner
import com.grappim.docuvault.feature.docgroup.repoapi.GroupRepository
import com.grappim.docuvault.feature.docgroup.uiapi.GroupUIMapper
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.feature.docs.repoapi.models.CreateDocument
import com.grappim.docuvault.feature.docs.repoapi.usecase.CancelEditDocumentChangesUseCase
import com.grappim.docuvault.feature.docs.repoapi.usecase.EditDocumentFinalizeUseCase
import com.grappim.docuvault.feature.docs.repoapi.usecase.EditDocumentPreparationUseCase
import com.grappim.docuvault.testing.MainDispatcherRule
import com.grappim.docuvault.testing.SavedStateHandleRule
import com.grappim.docuvault.testing.getDocumentFile
import com.grappim.docuvault.testing.getDocumentFileUI
import com.grappim.docuvault.testing.getDraftDocument
import com.grappim.docuvault.testing.getGroup
import com.grappim.docuvault.testing.getGroupUI
import com.grappim.docuvault.testing.getRandomFile
import com.grappim.docuvault.testing.getRandomString
import com.grappim.docuvault.testing.getRandomUri
import com.grappim.docuvault.utils.filesapi.FilesPersistenceManager
import com.grappim.docuvault.utils.filesapi.deletion.FileDeletionUtils
import com.grappim.docuvault.utils.filesapi.mappers.FileDataMapper
import com.grappim.docuvault.utils.filesapi.models.CameraTakePictureData
import com.grappim.docuvault.utils.filesapi.urimanager.FileUriManager
import com.grappim.docuvault.utils.ui.NativeText
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
internal class DocumentManagerViewModelDraftTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var sut: DocumentManagerViewModel

    private val groupRepository = mockk<GroupRepository>()
    private val documentRepository = mockk<DocumentRepository>()
    private val fileUriManager = mockk<FileUriManager>()
    private val fileDeletionUtils = mockk<FileDeletionUtils>()
    private val fileDataMapper = mockk<FileDataMapper>()
    private val filesPersistenceManager = mockk<FilesPersistenceManager>()
    private val dataCleaner = mockk<DataCleaner>()
    private val groupUIMapper = mockk<GroupUIMapper>()
    private val editDocumentPreparationUseCase = mockk<EditDocumentPreparationUseCase>()
    private val editDocumentFinalizeUseCase = mockk<EditDocumentFinalizeUseCase>()
    private val cancelEditDocumentChangesUseCase = mockk<CancelEditDocumentChangesUseCase>()

    private val route = DocManagerNavRoute()

    @get:Rule
    val savedStateHandleRule = SavedStateHandleRule(route)

    private val groups = listOf(getGroup(), getGroup())
    private val uiGroups = listOf(getGroupUI(), getGroupUI())
    private val uiGroup = getGroupUI()
    private val draftDoc = getDraftDocument()
    private val isEdit = false

    @Before
    fun setup() {
        coEvery { groupRepository.getGroups() } returns flowOf(groups)
        coEvery { groupUIMapper.toGroupUIList(groups) } returns uiGroups
        coEvery { documentRepository.addDraftDocument() } returns draftDoc
        coEvery { groupUIMapper.toGroupUI(draftDoc.group) } returns uiGroup

        sut = DocumentManagerViewModel(
            groupRepository = groupRepository,
            documentRepository = documentRepository,
            fileUriManager = fileUriManager,
            fileDeletionUtils = fileDeletionUtils,
            fileDataMapper = fileDataMapper,
            filesPersistenceManager = filesPersistenceManager,
            dataCleaner = dataCleaner,
            groupUIMapper = groupUIMapper,
            editDocumentPreparationUseCase = editDocumentPreparationUseCase,
            editDocumentFinalizeUseCase = editDocumentFinalizeUseCase,
            cancelEditDocumentChangesUseCase = cancelEditDocumentChangesUseCase,
            savedStateHandle = savedStateHandleRule.savedStateHandleMock
        )
    }

    @Test
    fun `on init should prepareGroups and prepareDraftDocument`() = runTest {
        groupRepository.getGroups().test {
            coVerify { groupUIMapper.toGroupUIList(awaitItem()) }
            assertEquals(uiGroups, sut.viewState.value.groups)
            awaitComplete()
        }

        coVerify { documentRepository.addDraftDocument() }

        assertEquals(draftDoc, sut.viewState.value.draftDocument)
        assertEquals(uiGroup, sut.viewState.value.selectedGroup)
        assertIs<NativeText.Resource>(sut.viewState.value.bottomBarButtonText)
        assertIs<NativeText.Resource>(sut.viewState.value.alertDialogText)
    }

    @Test
    fun `on onGroupSelected should update selectedGroup`() {
        assertEquals(uiGroup, sut.viewState.value.selectedGroup)

        val newUiGroup = getGroupUI()
        sut.viewState.value.onGroupSelected(newUiGroup)
        assertEquals(newUiGroup, sut.viewState.value.selectedGroup)
    }

    @Test
    fun `on getCameraImageFileUri should call fileUriManager`() {
        val folderName = draftDoc.documentFolderName
        val expected = CameraTakePictureData(
            uri = getRandomUri(),
            file = getRandomFile()
        )
        every { fileUriManager.getFileUriForTakePicture(folderName, isEdit) } returns expected

        val actual = sut.viewState.value.getCameraImageFileUri()

        assertEquals(expected, actual)
        verify { fileUriManager.getFileUriForTakePicture(folderName, isEdit) }
    }

    @Test
    fun `on setName should update documentName`() {
        val name = getRandomString()
        sut.viewState.value.setName(name)
        assertEquals(name, sut.viewState.value.documentName)
    }

    @Test
    fun `on addImageFromGallery should call fileUriManager`() {
        val uri = getRandomUri()
        val expected = getDocumentFileUI()
        coEvery {
            fileUriManager.getDocumentFileData(
                uri,
                draftDoc.documentFolderName,
                isEdit
            )
        } returns expected

        sut.viewState.value.onAddImageFromGalleryClicked(uri)

        assertEquals(listOf(expected), sut.viewState.value.files)
        coVerify { fileUriManager.getDocumentFileData(uri, draftDoc.documentFolderName, isEdit) }
    }

    @Test
    fun `on addCameraPicture should add new picture to the files`() {
        val cameraTakePictureData = CameraTakePictureData(
            uri = getRandomUri(),
            file = getRandomFile()
        )
        val expected = getDocumentFileUI()
        coEvery {
            fileUriManager.getFileDataFromCameraPicture(
                cameraTakePictureData,
                isEdit
            )
        } returns expected

        sut.viewState.value.onAddCameraPictureClicked(cameraTakePictureData)

        assertEquals(listOf(expected), sut.viewState.value.files)
        coVerify { fileUriManager.getFileDataFromCameraPicture(cameraTakePictureData, isEdit) }
    }

    @Test
    fun `on addMultipleUris should add new pictures to the files`() {
        val uris = listOf(getRandomUri(), getRandomUri())
        val expected = listOf(getDocumentFileUI(), getDocumentFileUI())
        coEvery {
            fileUriManager.getDocumentFileDataList(
                uris,
                draftDoc.documentFolderName,
                isEdit
            )
        } returns expected

        sut.viewState.value.onMultipleFilesAdded(uris)

        assertEquals(expected, sut.viewState.value.files)
        coVerify {
            fileUriManager.getDocumentFileDataList(
                uris,
                draftDoc.documentFolderName,
                isEdit
            )
        }
    }

    @Test
    fun `on onDocumentDone with empty name should set snackbar message`() = runTest {
        sut.snackBarMessage.test {
            sut.viewState.value.setName("")
            sut.viewState.value.onDocumentDone()

            val item = awaitItem()
            assertIs<NativeText.Resource>(item)
            assertEquals(NativeText.Resource(R.string.set_name), item)
        }
    }

    @Test
    fun `on onDocumentDone with empty files should set snackbar message`() = runTest {
        sut.snackBarMessage.test {
            sut.viewState.value.setName(getRandomString())
            sut.viewState.value.onDocumentDone()

            val item = awaitItem()
            assertIs<NativeText.Resource>(item)
            assertEquals(NativeText.Resource(R.string.add_file), item)
        }
    }

    @Test
    fun `on onDocumentDone should save new document`() = runTest {
        val mappedUri = getDocumentFileUI()
        val files = listOf(mappedUri)
        val docFiles = listOf(getDocumentFile())
        val selectedGroup = getGroupUI()
        val group = getGroup()
        val docName = getRandomString()
        val desc = getRandomString()
        val uriToAdd = getRandomUri()
        val createDoc = CreateDocument(
            id = draftDoc.id,
            name = docName,
            group = group,
            files = docFiles,
            createdDate = draftDoc.date,
            documentFolderName = draftDoc.documentFolderName,
            description = desc
        )

        coEvery { fileDataMapper.toDocumentFileDataList(files) } returns docFiles
        coEvery { groupUIMapper.toGroup(selectedGroup) } returns group
        coEvery { documentRepository.addDocument(createDoc) } just Runs

        coEvery {
            fileUriManager.getDocumentFileData(
                uri = uriToAdd,
                folderName = draftDoc.documentFolderName,
                isEdit = isEdit
            )
        } returns mappedUri

        sut.viewState.value.onGroupSelected(selectedGroup)
        sut.viewState.value.setName(docName)
        sut.viewState.value.onAddImageFromGalleryClicked(uriToAdd)

        sut.viewState.value.onDocumentDone()

        coVerify { fileDataMapper.toDocumentFileDataList(files) }
        coVerify { groupUIMapper.toGroup(selectedGroup) }
        coVerify { documentRepository.addDocument(createDoc) }
        assertTrue(sut.viewState.value.isNewDocument)
        assertTrue(sut.viewState.value.documentSaved)
    }
}
