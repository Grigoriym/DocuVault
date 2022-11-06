package com.grappim.docsofmine.ui.screens.main.groups.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.docsofmine.utils.states.CreateGroupStateViewModel
import com.grappim.docsofmine.utils.states.CreateGroupStateViewModelImpl
import com.grappim.domain.GroupToCreate
import com.grappim.domain.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel(),
    CreateGroupStateViewModel by CreateGroupStateViewModelImpl() {

    fun createGroup() {
        viewModelScope.launch {
            groupRepository.createGroup(
                GroupToCreate(
                    name = createGroupInputState.value.name,
                    fields = emptyList(),
                    color = createGroupInputState.value.colorString
                )
            )
        }
    }
}