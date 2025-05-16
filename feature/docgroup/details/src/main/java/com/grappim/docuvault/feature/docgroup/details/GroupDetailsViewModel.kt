package com.grappim.docuvault.feature.docgroup.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.grappim.docuvault.core.navigation.GroupDetailsNavRoute
import com.grappim.docuvault.feature.docgroup.repoapi.GroupRepository
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.utils.filesapi.mappers.DocsListUIMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val groupRepository: GroupRepository,
    private val docRepository: DocumentRepository,
    private val docsListUIMapper: DocsListUIMapper
) : ViewModel() {

    private val _viewState = MutableStateFlow(GroupDetailsState())

    val viewState = _viewState.asStateFlow()

    private val groupDetailsNavRoute = savedStateHandle.toRoute<GroupDetailsNavRoute>()

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            val groupId = groupDetailsNavRoute.groupId
            val group = groupRepository.getGroupById(groupId)
            val documents = docRepository.getDocumentsByGroupId(groupId)
            val uiDocuments = docsListUIMapper.toDocumentListUIList(documents)
            _viewState.update {
                it.copy(group = group, documents = uiDocuments)
            }
        }
    }
}
