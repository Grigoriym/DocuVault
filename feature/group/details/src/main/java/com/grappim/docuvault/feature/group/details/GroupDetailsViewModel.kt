package com.grappim.docuvault.feature.group.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.docuvault.core.navigation.GroupNavDestinations
import com.grappim.docuvault.feature.group.repoapi.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val groupRepository: GroupRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow(GroupDetailsState())

    val viewState = _viewState.asStateFlow()

    private val groupId: String
        get() = requireNotNull(savedStateHandle[GroupNavDestinations.GroupDetails.KEY_GROUP_ID])

    init {
        fetchGroup()
    }

    private fun fetchGroup() {
        viewModelScope.launch {
            val group = groupRepository.getGroupById(groupId)
            _viewState.update {
                it.copy(group = group)
            }
        }
    }
}
