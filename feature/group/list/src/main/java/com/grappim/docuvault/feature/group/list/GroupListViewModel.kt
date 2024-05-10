package com.grappim.docuvault.feature.group.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.docuvault.feature.group.repoapi.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow(GroupListState())

    val viewState = _viewState.asStateFlow()

    init {
        fetchGroups()
    }

    private fun fetchGroups() {
        viewModelScope.launch {
            groupRepository.getGroups()
                .onEach { list ->
                    _viewState.update { it.copy(groups = list) }
                }.collect()
        }
    }
}
