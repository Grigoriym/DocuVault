package com.grappim.docsofmine.ui.screens.main.docs.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.docsofmine.navigation.DocumentsNavDestinations
import com.grappim.docsofmine.utils.WhileViewSubscribed
import com.grappim.domain.Document
import com.grappim.domain.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DocumentDetailsViewModel @Inject constructor(
    documentRepository: DocumentRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val document: StateFlow<Document?> = documentRepository.getDocumentById(
        savedStateHandle
            .get<String>(DocumentsNavDestinations.Details.documentIdArgument())
            ?.toLong()!!
    )
        .stateIn(
            scope = viewModelScope,
            started = WhileViewSubscribed,
            initialValue = null
        )


}