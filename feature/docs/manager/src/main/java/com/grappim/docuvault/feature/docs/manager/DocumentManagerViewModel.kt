package com.grappim.docuvault.feature.docs.manager

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.docuvault.core.navigation.DocumentsNavDestinations
import com.grappim.docuvault.data.backupapi.BackupFilesRepository
import com.grappim.docuvault.data.cleanerapi.DataCleaner
import com.grappim.docuvault.feature.docs.domain.CreateDocument
import com.grappim.docuvault.feature.docs.domain.Document
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.feature.docs.uiapi.DocumentFileUI
import com.grappim.docuvault.feature.group.repoapi.GroupRepository
import com.grappim.docuvault.feature.group.uiapi.GroupUI
import com.grappim.docuvault.feature.group.uiapi.GroupUIMapper
import com.grappim.docuvault.utils.files.deletion.FileDeletionUtils
import com.grappim.docuvault.utils.files.docfilemanager.DocumentFileManager
import com.grappim.docuvault.utils.files.docfiles.FilesPersistenceManager
import com.grappim.docuvault.utils.files.mappers.FileDataMapper
import com.grappim.docuvault.utils.files.models.CameraTakePictureData
import com.grappim.docuvault.utils.files.urimanager.FileUriManager
import com.grappim.docuvault.utils.ui.NativeText
import com.grappim.docuvault.utils.ui.SnackbarStateViewModel
import com.grappim.docuvault.utils.ui.SnackbarStateViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DocumentManagerViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val documentRepository: DocumentRepository,
    private val fileUriManager: FileUriManager,
    private val fileDeletionUtils: FileDeletionUtils,
    private val fileDataMapper: FileDataMapper,
    private val documentFileManager: DocumentFileManager,
    private val backupFilesRepository: BackupFilesRepository,
    private val filesPersistenceManager: FilesPersistenceManager,
    private val dataCleaner: DataCleaner,
    private val groupUIMapper: GroupUIMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    SnackbarStateViewModel by SnackbarStateViewModelImpl() {

    private val _viewState = MutableStateFlow(
        DocumentManagerState(
            setName = ::setName,
            onAddImageFromGalleryClicked = ::addImageFromGallery,
            onAddCameraPictureClicked = ::addCameraPicture,
            getCameraImageFileUri = ::getCameraImageFileUri,
            onGroupSelected = ::onGroupSelected,
            onFileRemoved = ::removeFile,
            onMultipleFilesAdded = ::addMultipleUris,
            onDocumentDone = ::onDocumentDone,
            onQuit = ::onQuit,
            onForceQuit = ::onForceQuit,
            onShowAlertDialog = ::onShowAlertDialog
        )
    )
    val viewState = _viewState.asStateFlow()

    private val editDocumentId: String? =
        savedStateHandle[DocumentsNavDestinations.DocManager.KEY_EDIT_DOC_ID]

    private val editDocumentIdLong: Long
        get() = requireNotNull(editDocumentId?.toLong())

    private val documentFolderName: String
        get() = if (_viewState.value.isNewDocument) {
            requireNotNull(viewState.value.draftDocument).documentFolderName
        } else {
            requireNotNull(_viewState.value.editDocument).documentFolderName
        }

    init {
        prepareGroups()
        if (isEdit()) {
            prepareDocumentToEdit()
        } else {
            prepareDraftDocument()
        }
    }

    private fun isEdit(): Boolean = editDocumentId?.isNotEmpty() == true

    private fun prepareGroups() {
        viewModelScope.launch {
            groupRepository.getGroups()
                .collect { groups ->
                    _viewState.update {
                        it.copy(groups = groupUIMapper.toGroupUIList(groups))
                    }
                }
        }
    }

    private fun prepareDraftDocument() {
        viewModelScope.launch {
            val draftDoc = documentRepository.addDraftDocument()
            _viewState.update {
                it.copy(
                    draftDocument = draftDoc,
                    selectedGroup = groupUIMapper.toGroupUI(draftDoc.group),
                    bottomBarButtonText = NativeText.Resource(R.string.create),
                    alertDialogText = NativeText.Resource(R.string.if_quit_lose_data)
                )
            }
        }
    }

    private fun prepareDocumentToEdit() {
        viewModelScope.launch {
            val editDocument = documentRepository.getDocumentById(editDocumentIdLong)

            documentFileManager.copyToBackupFolder(
                documentFolderName = editDocument.documentFolderName
            )

            backupFilesRepository.insertFiles(
                documentId = editDocumentIdLong,
                files = editDocument.files
            )

            val uiFiles = fileDataMapper.toDocumentFileUiDataList(editDocument.files)

            _viewState.update {
                it.copy(
                    files = uiFiles,
                    documentName = editDocument.name,
                    isNewDocument = false,
                    editDocument = editDocument,
                    selectedGroup = groupUIMapper.toGroupUI(editDocument.group),
                    bottomBarButtonText = NativeText.Resource(R.string.save),
                    alertDialogText = NativeText.Resource(R.string.if_quit_ensure_saved)
                )
            }
        }
    }

    private fun onGroupSelected(groupUI: GroupUI) {
        _viewState.update {
            it.copy(
                selectedGroup = groupUI
            )
        }
    }

    private fun getCameraImageFileUri(): CameraTakePictureData =
        fileUriManager.getFileUriForTakePicture(
            folderName = documentFolderName,
            isEdit = isEdit()
        )

    private fun setName(name: String) {
        _viewState.update {
            it.copy(documentName = name)
        }
    }

    private fun addImageFromGallery(uri: Uri) {
        viewModelScope.launch {
            val documentFileUiData = fileUriManager.getDocumentFileData(
                uri = uri,
                folderName = documentFolderName,
                isEdit = isEdit()
            )
            addImageData(documentFileUiData)
        }
    }

    private fun addCameraPicture(cameraTakePictureData: CameraTakePictureData) {
        viewModelScope.launch {
            val documentFileUiData = fileUriManager.getFileDataFromCameraPicture(
                cameraTakePictureData = cameraTakePictureData,
                isEdit = isEdit()
            )
            addImageData(documentFileUiData)
        }
    }

    private fun addImageData(documentFileUI: DocumentFileUI) {
        val result = _viewState.value.files + documentFileUI
        _viewState.update {
            it.copy(files = result)
        }
    }

    private fun addImageData(list: List<DocumentFileUI>) {
        val result = _viewState.value.files + list
        _viewState.update {
            it.copy(files = result)
        }
    }

    private fun addMultipleUris(uris: List<Uri>) {
        viewModelScope.launch {
            val list = fileUriManager.getDocumentFileDataList(
                uriList = uris,
                folderName = documentFolderName,
                isEdit = isEdit()
            )
            addImageData(list)
        }
    }

    private fun saveNewDocument() {
        viewModelScope.launch {
            val currentDraft = requireNotNull(_viewState.value.draftDocument)
            val name = _viewState.value.documentName.trim()
            val selectedGroup = requireNotNull(_viewState.value.selectedGroup)
            val files = fileDataMapper.toDocumentFileDataList(_viewState.value.files)

            documentRepository.addDocument(
                CreateDocument(
                    id = currentDraft.id,
                    name = name,
                    group = groupUIMapper.toGroup(selectedGroup),
                    files = files,
                    createdDate = currentDraft.date,
                    documentFolderName = currentDraft.documentFolderName
                )
            )
            _viewState.update {
                it.copy(documentSaved = true)
            }
        }
    }

    private fun onDocumentDone() {
        viewModelScope.launch {
            when {
                _viewState.value.documentName.isEmpty() -> {
                    setSnackbarMessageSuspend(NativeText.Resource(R.string.set_name))
                }

                _viewState.value.files.isEmpty() -> {
                    setSnackbarMessageSuspend(NativeText.Resource(R.string.add_file))
                }

                else -> {
                    if (_viewState.value.isNewDocument) {
                        saveNewDocument()
                    } else {
                        editDocument()
                    }
                }
            }
        }
    }

    private fun editDocument() {
        viewModelScope.launch {
            val name = _viewState.value.documentName.trim()
            val group = requireNotNull(_viewState.value.selectedGroup)
            val editedFiles = filesPersistenceManager
                .prepareEditedFilesToPersist(_viewState.value.files)

            val editProduct = requireNotNull(_viewState.value.editDocument)
            val document = Document(
                documentId = editDocumentIdLong,
                name = name,
                createdDate = editProduct.createdDate,
                group = groupUIMapper.toGroup(group),
                files = editedFiles,
                documentFolderName = editProduct.documentFolderName
            )

            documentFileManager.moveFromTempToOriginalFolder(documentFolderName)
            dataCleaner.deleteTempFolder(
                documentFolderName = documentFolderName
            )

            dataCleaner.deleteBackupFolder(
                documentFolderName = documentFolderName
            )
            backupFilesRepository.deleteFilesByDocumentId(editDocumentIdLong)

            documentRepository.updateDocumentWithFiles(document, editedFiles)

            _viewState.update {
                it.copy(documentSaved = true)
            }
        }
    }

    private fun removeFile(documentFileUI: DocumentFileUI) {
        viewModelScope.launch {
            if (fileDeletionUtils.deleteFile(uri = documentFileUI.uri)) {
                Timber.d("file removed: $documentFileUI")

                if (!_viewState.value.isNewDocument) {
                    documentRepository.deleteDocumentFile(
                        documentId = editDocumentId!!.toLong(),
                        fileName = documentFileUI.name
                    )
                }

                _viewState.update { currentState ->
                    val updatedFilesUris = currentState.files.filterNot { it == documentFileUI }
                    currentState.copy(files = updatedFilesUris)
                }
            }
        }
    }

    private fun onQuit() {
        viewModelScope.launch {
            _viewState.update { it.copy(quitStatus = QuitStatus.InProgress) }
            if (_viewState.value.isNewDocument) {
                val draftDocument = requireNotNull(viewState.value.draftDocument)
                dataCleaner.deleteDocumentData(
                    documentId = draftDocument.id,
                    documentFolderName = draftDocument.documentFolderName
                )
            } else {
                val initialFiles = backupFilesRepository.getAllByDocumentId(editDocumentIdLong)
                documentRepository.updateFilesInDocument(editDocumentIdLong, initialFiles)
                documentFileManager.moveFromBackupToOriginalFolder(documentFolderName)

                dataCleaner.deleteTempFolder(documentFolderName)
                dataCleaner.deleteBackupFolder(documentFolderName)

                backupFilesRepository.deleteFilesByDocumentId(editDocumentIdLong)
            }
            _viewState.update { it.copy(quitStatus = QuitStatus.Finish) }
        }
    }

    private fun onForceQuit() {
        _viewState.update { it.copy(forceQuit = true) }
    }

    private fun onShowAlertDialog(show: Boolean) {
        _viewState.update {
            it.copy(
                showAlertDialog = show
            )
        }
    }
}
