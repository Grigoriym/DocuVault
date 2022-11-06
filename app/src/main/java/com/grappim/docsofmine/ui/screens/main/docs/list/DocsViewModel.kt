package com.grappim.docsofmine.ui.screens.main.docs.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.docsofmine.utils.WhileViewSubscribed
import com.grappim.domain.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DocsViewModel @Inject constructor(
    documentRepository: DocumentRepository
) : ViewModel() {

    val documents = documentRepository.getAllDocumentsFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileViewSubscribed,
            initialValue = emptyList()
        )

}