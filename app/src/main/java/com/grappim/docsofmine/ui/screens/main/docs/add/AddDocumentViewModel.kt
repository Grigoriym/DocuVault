package com.grappim.docsofmine.ui.screens.main.docs.add

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.docsofmine.data.DataCleaner
import com.grappim.docsofmine.uikit.MimeTypeImageHelper
import com.grappim.docsofmine.uikit.R
import com.grappim.docsofmine.utils.NativeText
import com.grappim.docsofmine.utils.files.CameraTakePictureData
import com.grappim.docsofmine.utils.files.FileData
import com.grappim.docsofmine.utils.files.FileUtils
import com.grappim.docsofmine.utils.files.mime.MimeTypes
import com.grappim.docsofmine.utils.states.SnackbarStateViewModel
import com.grappim.docsofmine.utils.states.SnackbarStateViewModelImpl
import com.grappim.domain.model.document.CreateDocument
import com.grappim.domain.model.document.DocumentFileData
import com.grappim.domain.model.document.DraftDocument
import com.grappim.domain.model.group.Group
import com.grappim.domain.repository.DocumentRepository
import com.grappim.domain.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDocumentViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val documentRepository: DocumentRepository,
    private val fileUtils: FileUtils,
    private val mimeTypeImageHelper: MimeTypeImageHelper,
    private val dataCleaner: DataCleaner
) : ViewModel(),
    SnackbarStateViewModel by SnackbarStateViewModelImpl() {

    private val _documentName = MutableStateFlow("")
    val documentName: StateFlow<String>
        get() = _documentName.asStateFlow()

    private val _documentCreated = MutableStateFlow(false)
    val documentCreated: StateFlow<Boolean>
        get() = _documentCreated.asStateFlow()

    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>>
        get() = _groups.asStateFlow()

    private val _selectedGroup = MutableStateFlow<Group?>(null)
    val selectedGroup: StateFlow<Group?>
        get() = _selectedGroup.asStateFlow()

    private val _filesUris = mutableStateListOf<FileData>()
    val filesUris
        get() = _filesUris

    private val _draftDocument = MutableStateFlow<DraftDocument?>(null)
    private val draftDocument: Flow<DraftDocument>
        get() = _draftDocument.filterNotNull()

    init {
        getGroups()
        addDraftDoc()
    }

    private fun getGroups() {
        viewModelScope.launch {
            groupRepository.getGroups()
                .collect {
                    _groups.value = it
                    _selectedGroup.value = it.first()
                }
        }
    }

    private fun addDraftDoc() {
        viewModelScope.launch {
            _draftDocument.value = documentRepository.addDraftDocument()
        }
    }

    fun setDocumentName(name: String) {
        _documentName.value = name
    }

    fun setGroup(group: Group) {
        _selectedGroup.value = group
    }

    fun addDocumentsFromGallery(uris: List<Uri>) {
        viewModelScope.launch {
            val result = uris.map {
                fileUtils.getFileUrisFromGalleryUri(
                    uri = it,
                    draftDocument = draftDocument.first()
                )
            }
            _filesUris.addAll(result)
        }
    }

    fun addDocuments(uris: List<Uri>) {
        viewModelScope.launch {
            val result = uris.map {
                val fileData = fileUtils.getFileUrisFromUri(
                    uri = it,
                    draftDocument = draftDocument.first()
                )
                if (fileData.preview == null) {
                    fileData.preview = mimeTypeImageHelper
                        .getImageByMimeType(fileData.mimeType)
                }
                fileData
            }
            _filesUris.addAll(result)
        }
    }

    fun addCameraPicture(cameraTakePictureData: CameraTakePictureData) {
        viewModelScope.launch {
            val result = fileUtils.getFileDataFromCameraPicture(
                cameraTakePictureData = cameraTakePictureData
            )
            _filesUris.add(result)
        }
    }

    fun getCameraImageFileUri(): CameraTakePictureData {
        return fileUtils.getFileUriForTakePicture(_draftDocument.value!!.folderName)
    }

    fun saveData() {
        viewModelScope.launch {
            saveDocument()
        }
    }

    fun removeData() {
        viewModelScope.launch {
            val draftDoc = draftDocument.first()
            dataCleaner.clearDocumentData(draftDoc)
        }
    }

    private fun saveDocument() {
        viewModelScope.launch {
            val name = documentName.value.ifEmpty {
                draftDocument.first().folderName
            }

            documentRepository.addDocument(
                CreateDocument(
                    id = draftDocument.first().id,
                    name = name,
                    group = requireNotNull(selectedGroup.value),
                    filesUri = filesUris.toList().map {
                        val previewUri: Uri? = if (it.mimeType in MimeTypes.images ||
                            it.mimeType == MimeTypes.Application.PDF
                        ) {
                            it.preview as? Uri
                        } else {
                            null
                        }

                        DocumentFileData(
                            name = it.name,
                            mimeType = it.mimeType,
                            uriPath = it.uri.path ?: "",
                            uriString = it.uri.toString(),
                            size = it.size,
                            previewUriString = previewUri?.toString(),
                            previewUriPath = previewUri?.path,
                            md5 = it.md5
                        )
                    },
                    createdDate = draftDocument.first().date
                )
            )
            _documentCreated.value = true
        }
    }

    fun createDocument() {
        viewModelScope.launch {
            when {
                documentName.value.isEmpty() -> {
                    setSnackbarMessageSuspend(NativeText.Resource(R.string.set_name))
                }
                filesUris.isEmpty() -> {
                    setSnackbarMessageSuspend(NativeText.Resource(R.string.add_file))
                }
                else -> {
                    saveDocument()
                }
            }
        }
    }

    fun removeFile(fileData: FileData) {
        if (fileUtils.removeFile(fileData)) {
            _filesUris.remove(fileData)
        }
    }
}