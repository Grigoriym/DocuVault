package com.grappim.docuvault.feature.docs.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.utils.files.mappers.DocsListUIMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocsViewModel @Inject constructor(
    private val documentRepository: DocumentRepository,
    private val docsListUIMapper: DocsListUIMapper
) : ViewModel() {

    private val _viewState = MutableStateFlow(
        DocsListState()
    )

    val viewState = _viewState.asStateFlow()

    init {
        getDocuments()
    }

    private fun getDocuments() {
        viewModelScope.launch {
            documentRepository.getAllDocumentsFlow().map { list ->
                docsListUIMapper.toDocumentListUIList(list)
            }.onEach { list ->
                _viewState.update {
                    it.copy(documents = list)
                }
            }.collect()
        }
    }
}
