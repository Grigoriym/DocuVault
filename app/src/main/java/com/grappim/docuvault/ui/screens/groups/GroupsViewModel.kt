package com.grappim.docuvault.ui.screens.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.docuvault.utils.WhileViewSubscribed
import com.grappim.domain.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    val groups = groupRepository.getGroups()
        .stateIn(
            scope = viewModelScope,
            started = WhileViewSubscribed,
            initialValue = emptyList()
        )
}
