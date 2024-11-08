package com.grappim.docuvault.feature.docs.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.docuvault.core.navigation.DocumentsNavDestinations
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.utils.files.mappers.FileDataMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentDetailsViewModel @Inject constructor(
    private val documentRepository: DocumentRepository,
    private val savedStateHandle: SavedStateHandle,
    private val fileDataMapper: FileDataMapper
) : ViewModel() {

    private val _viewState = MutableStateFlow(
        DocumentDetailsState(
            updateProduct = ::updateProduct
        )
    )
    val viewState = _viewState.asStateFlow()

    private val documentId: Long
        get() = requireNotNull(savedStateHandle[DocumentsNavDestinations.Details.KEY_DOC_ID])

    init {
        getDocument()
    }

    private fun getDocument() {
        viewModelScope.launch {
            val document = documentRepository.getDocumentById(documentId)
            _viewState.update {
                it.copy(
                    document = document,
                    files = fileDataMapper.toDocumentFileUiDataList(document.files)
                )
            }
        }
    }

    private fun updateProduct() {
        getDocument()
    }
}
