package com.grappim.docsofmine.ui.screens.main.docs.add

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.docsofmine.uikit.R
import com.grappim.docsofmine.utils.NativeText
import com.grappim.docsofmine.utils.files.FileUris
import com.grappim.docsofmine.utils.files.FileUtils
import com.grappim.docsofmine.utils.states.SnackbarStateViewModel
import com.grappim.docsofmine.utils.states.SnackbarStateViewModelImpl
import com.grappim.domain.CreateDocument
import com.grappim.domain.DocumentFileUri
import com.grappim.domain.DraftDocument
import com.grappim.domain.Group
import com.grappim.domain.repository.DocumentRepository
import com.grappim.domain.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDocumentViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val documentRepository: DocumentRepository,
    private val fileUtils: FileUtils
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

    private val _filesUris = mutableStateListOf<FileUris>()
    val filesUris
        get() = _filesUris

    private val _draftDocument = MutableStateFlow<DraftDocument?>(null)
    val draftDocument: StateFlow<DraftDocument?>
        get() = _draftDocument.asStateFlow()

    init {
        getGroups()
        addDraftDoc()
    }

    fun getGroups() {
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
        val result = uris.map {
            fileUtils.getFileUrisFromGalleryUri(
                uri = it,
                draftDocument = _draftDocument.value!!
            )
        }
        _filesUris.addAll(result)
    }

    fun addDocuments(uris: List<Uri>) {
        val result = uris.map {
            fileUtils.getFileUrisFromUri(
                uri = it,
                draftDocument = _draftDocument.value!!
            )
        }
        _filesUris.addAll(result)
    }

    fun addPicture(cameraImageUri: Uri) {
        val result = fileUtils.getFileUrisFromUriPicture(
            uri = cameraImageUri,
            draftDocument = _draftDocument.value!!
        )
        _filesUris.add(result)
    }

    fun getCameraImageFileUri(): Uri {
        return fileUtils.getTmpFileUri(_draftDocument.value!!)
    }

    fun saveData() {
        viewModelScope.launch {
            saveDocument()
        }
    }

    fun removeData() {
        viewModelScope.launch {
            fileUtils.deleteFolder(_draftDocument.value!!)
            documentRepository.removeDocumentById(_draftDocument.value?.id!!)
        }
    }

    private fun saveDocument() {
        viewModelScope.launch {
            val name = if (documentName.value.isEmpty()) {
                _draftDocument.value?.folderName!!
            } else {
                documentName.value
            }

            documentRepository.addDocument(
                CreateDocument(
                    id = _draftDocument.value?.id!!,
                    name = name,
                    group = requireNotNull(selectedGroup.value),
                    filesUri = filesUris.toList().map {
                        DocumentFileUri(
                            fileName = it.fileName,
                            mimeType = it.mimeType,
                            path = it.fileUri.path ?: "",
                            string = it.fileUri.toString(),
                            size = it.fileSize
                        )
                    },
                    createdDate = _draftDocument.value?.date!!
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

    fun removeFile(fileUris: FileUris) {
        if (fileUtils.removeFile(fileUris)) {
            _filesUris.remove(fileUris)
        }
    }
}