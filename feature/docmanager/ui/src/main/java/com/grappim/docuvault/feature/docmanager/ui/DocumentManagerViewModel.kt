package com.grappim.docuvault.feature.docmanager.ui

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.docuvault.core.navigation.MainNavDestinations
import com.grappim.docuvault.feature.group.domain.Group
import com.grappim.docuvault.feature.group.repoapi.GroupRepository
import com.grappim.docuvault.repo.DataCleaner
import com.grappim.docuvault.uikit.DocumentFileUiData
import com.grappim.docuvault.uikit.R
import com.grappim.docuvault.utils.files.FileUtils
import com.grappim.docuvault.utils.files.deletion.FileDeletionUtils
import com.grappim.docuvault.utils.files.mappers.FileDataMapper
import com.grappim.docuvault.utils.files.models.CameraTakePictureData
import com.grappim.docuvault.utils.files.urimanager.FileUriManager
import com.grappim.docuvault.utils.ui.MimeTypeImageHelper
import com.grappim.docuvault.utils.ui.NativeText
import com.grappim.docuvault.utils.ui.SnackbarStateViewModel
import com.grappim.docuvault.utils.ui.SnackbarStateViewModelImpl
import com.grappim.domain.model.document.CreateDocument
import com.grappim.domain.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@Suppress("UnusedPrivateProperty")
class DocumentManagerViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val documentRepository: DocumentRepository,
    private val fileUtils: FileUtils,
    private val mimeTypeImageHelper: MimeTypeImageHelper,
    private val dataCleaner: DataCleaner,
    private val fileUriManager: FileUriManager,
    private val fileDeletionUtils: FileDeletionUtils,
    private val fileDataMapper: FileDataMapper,
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
            onDocumentDone = ::onDocumentDone
        )
    )
    val viewState = _viewState.asStateFlow()

    private val editDocumentId: String? =
        savedStateHandle[MainNavDestinations.ProductManager.KEY_EDIT_PRODUCT_ID]

    private val documentFolderName: String
        get() = if (_viewState.value.isNewDocument) {
            requireNotNull(viewState.value.draftDocument).documentFolderName
        } else {
            requireNotNull(_viewState.value.editDocument).documentFolderName
        }

    init {
        prepareDraftDocument()
        prepareGroups()
    }

    private fun prepareGroups() {
        viewModelScope.launch {
            groupRepository.getGroups()
                .collect { groups ->
                    _viewState.update {
                        it.copy(groups = groups)
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
                    selectedGroup = draftDoc.group
                )
            }
        }
    }

    private fun onGroupSelected(group: Group) {
        _viewState.update {
            it.copy(
                selectedGroup = group
            )
        }
    }

    private fun getCameraImageFileUri(): CameraTakePictureData =
        fileUriManager.getFileUriForTakePicture(
            folderName = documentFolderName,
            isEdit = editDocumentId?.isNotEmpty() == true
        )

    private fun setName(name: String) {
        _viewState.update {
            it.copy(documentName = name)
        }
    }

    private fun addImageFromGallery(uri: Uri) {
        viewModelScope.launch {
            val documentFileUiData = fileUriManager.getFileUriFromGalleryUri(
                uri = uri,
                folderName = documentFolderName,
                isEdit = editDocumentId?.isNotEmpty() == true
            )
            addImageData(documentFileUiData)
        }
    }

    private fun addCameraPicture(cameraTakePictureData: CameraTakePictureData) {
        viewModelScope.launch {
            val documentFileUiData = fileUriManager.getFileDataFromCameraPicture(
                cameraTakePictureData = cameraTakePictureData,
                isEdit = editDocumentId?.isNotEmpty() == true
            )
            addImageData(documentFileUiData)
        }
    }

    private fun addImageData(documentFileUiData: DocumentFileUiData) {
        val result = _viewState.value.files + documentFileUiData
        _viewState.update {
            it.copy(files = result)
        }
    }

    @Suppress("UnusedParameter")
    private fun addMultipleUris(uris: List<Uri>) {
        viewModelScope.launch {}
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
                    group = selectedGroup,
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

    @Suppress("EmptyElseBlock")
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
                    }
                }
            }
        }
    }

    private fun removeFile(documentFileUiData: DocumentFileUiData) {
        viewModelScope.launch {
            if (fileDeletionUtils.deleteFile(uri = documentFileUiData.uri)) {
                Timber.d("file removed: $documentFileUiData")

                if (!_viewState.value.isNewDocument) {
                    documentRepository.deleteDocumentFile(
                        documentId = editDocumentId!!.toLong(),
                        fileName = documentFileUiData.name
                    )
                }

                _viewState.update { currentState ->
                    val updatedFilesUris = currentState.files.filterNot { it == documentFileUiData }
                    currentState.copy(files = updatedFilesUris)
                }
            }
        }
    }
}
