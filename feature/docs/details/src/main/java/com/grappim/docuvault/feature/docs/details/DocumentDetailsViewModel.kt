package com.grappim.docuvault.feature.docs.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.grappim.docuvault.core.navigation.destinations.DocDetailsNavRoute
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.feature.docs.uiapi.DocumentFileUI
import com.grappim.docuvault.utils.androidapi.intent.IntentGenerator
import com.grappim.docuvault.utils.filesapi.mappers.FileDataMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val documentRepository: DocumentRepository,
    private val fileDataMapper: FileDataMapper,
    private val intentGenerator: IntentGenerator
) : ViewModel() {

    private val _viewState = MutableStateFlow(
        DocumentDetailsState(
            updateProduct = ::updateProduct,
            onFileClicked = ::onFileClicked
        )
    )
    val viewState = _viewState.asStateFlow()

    private val documentDetailsRoute = savedStateHandle.toRoute<DocDetailsNavRoute>()

    init {
        getDocument()
    }

    private fun getDocument() {
        viewModelScope.launch {
            val document = documentRepository.getDocumentById(documentDetailsRoute.documentId)
            _viewState.update {
                it.copy(
                    document = document,
                    files = fileDataMapper.toDocumentFileUiDataList(document.files)
                )
            }
        }
    }

    private fun onFileClicked(file: DocumentFileUI) {
        val intent = intentGenerator.getOpenFileIntent(file.uri, file.mimeType)
        _viewState.update {
            it.copy(openImageIntent = intent)
        }
    }

    private fun updateProduct() {
        getDocument()
    }
}
