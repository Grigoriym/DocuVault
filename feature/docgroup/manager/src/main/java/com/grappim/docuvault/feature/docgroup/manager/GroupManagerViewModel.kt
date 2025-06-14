package com.grappim.docuvault.feature.docgroup.manager

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.grappim.docuvault.core.navigation.destinations.GroupManagerNavRoute
import com.grappim.docuvault.feature.docgroup.repoapi.GroupRepository
import com.grappim.docuvault.feature.docgroup.repoapi.model.Group
import com.grappim.docuvault.feature.docgroup.repoapi.model.GroupToCreate
import com.grappim.docuvault.uikit.theme.Dom_Aero
import com.grappim.docuvault.uikit.utils.ColorUtils
import com.grappim.docuvault.utils.ui.NativeText
import com.grappim.docuvault.utils.ui.SnackbarStateViewModel
import com.grappim.docuvault.utils.ui.SnackbarStateViewModelImpl
import com.grappim.feature.docgroup.manager.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupManagerViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val colorUtils: ColorUtils,
    savedStateHandle: SavedStateHandle
) : ViewModel(), SnackbarStateViewModel by SnackbarStateViewModelImpl() {

    private val groupManagerNavRoute = savedStateHandle.toRoute<GroupManagerNavRoute>()

    private val groupId: Long? = groupManagerNavRoute.groupId

    private val _viewState = MutableStateFlow(
        GroupManagerState(
            color = Dom_Aero,
            setName = ::setName,
            setColor = ::setColor,
            onGroupDone = ::onGroupDone,
            doneButtonText = doneButtonText,
            isNewGroup = groupId == null
        )
    )
    val viewState = _viewState.asStateFlow()

    /**
     * When we edit a group, we are sure that the groupId is not null
     */
    private val editGroupId
        get() = requireNotNull(groupId)

    private val doneButtonText: NativeText
        get() = if (groupId == null) {
            NativeText.Resource(R.string.create)
        } else {
            NativeText.Resource(R.string.update)
        }

    init {
        if (groupId == null) {
            prepareDraftGroup()
        } else {
            prepareGroupToEdit()
        }
    }

    private fun prepareGroupToEdit() {
        viewModelScope.launch {
            val editGroup = groupRepository.getGroupById(editGroupId)
            _viewState.update {
                it.copy(
                    name = editGroup.name,
                    color = colorUtils.toComposeColor(editGroup.color)
                )
            }
        }
    }

    private fun prepareDraftGroup() {
        viewModelScope.launch { }
    }

    private fun onGroupDone() {
        viewModelScope.launch {
            if (viewState.value.name.isEmpty()) {
                setSnackbarMessageSuspend(NativeText.Resource(R.string.group_name_is_empty))
                return@launch
            }

            if (_viewState.value.isNewGroup) {
                createGroup()
            } else {
                updateGroup()
            }
        }
    }

    private fun createGroup() {
        viewModelScope.launch {
            groupRepository.createGroup(
                GroupToCreate(
                    name = viewState.value.name.trim(),
                    fields = emptyList(),
                    color = colorUtils.toHexString(viewState.value.color)
                )
            )
            _viewState.update {
                it.copy(groupSaved = true)
            }
        }
    }

    private fun updateGroup() {
        viewModelScope.launch {
            val name = _viewState.value.name.trim()
            val color = colorUtils.toHexString(_viewState.value.color)
            val groupToSave = Group(
                id = editGroupId,
                name = name,
                fields = emptyList(),
                color = color
            )
            groupRepository.updateGroup(groupToSave)
            _viewState.update {
                it.copy(groupSaved = true)
            }
        }
    }

    private fun setName(newName: String) {
        _viewState.update {
            it.copy(name = newName)
        }
    }

    private fun setColor(newColor: Color) {
        _viewState.update {
            it.copy(color = newColor)
        }
    }
}
