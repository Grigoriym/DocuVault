package com.grappim.docuvault.ui.screens.docs.details

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.docuvault.navigation.DocumentsNavDestinations
import com.grappim.docuvault.utils.FilePreviewHelper
import com.grappim.docuvault.utils.WhileViewSubscribed
import com.grappim.docuvault.utils.files.FileData
import com.grappim.docuvault.utils.files.FileUtils
import com.grappim.docuvault.utils.files.mime.MimeTypes
import com.grappim.domain.model.document.Document
import com.grappim.domain.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DocumentDetailsViewModel @Inject constructor(
    documentRepository: DocumentRepository,
    savedStateHandle: SavedStateHandle,
    private val fileUtils: FileUtils,
    private val filePreviewHelper: FilePreviewHelper
) : ViewModel() {

    val document: StateFlow<Document?> = documentRepository.getDocumentById(
        requireNotNull(
            savedStateHandle
                .get<String>(DocumentsNavDestinations.Details.documentIdArgument())
        ).toLong()
    ).stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = null
    )

    val filesData: Flow<List<FileData>> = document.map { document ->
        document?.filesUri?.map { documentFileData ->
            val uri = documentFileData.uriString.toUri()
            val mimeType = documentFileData.mimeType

            val preview = filePreviewHelper.getPreview(
                mimeType = mimeType,
                fileDataUriString = documentFileData.uriString,
                document = document
            )

            FileData(
                uri = uri,
                mimeType = mimeType,
                name = documentFileData.name,
                size = documentFileData.size,
                sizeToDemonstrate = fileUtils.formatFileSize(documentFileData.size),
                mimeTypeToDemonstrate = MimeTypes.formatMimeType(documentFileData.mimeType),
                preview = preview,
                md5 = documentFileData.md5
            )
        } ?: emptyList()
    }
}
