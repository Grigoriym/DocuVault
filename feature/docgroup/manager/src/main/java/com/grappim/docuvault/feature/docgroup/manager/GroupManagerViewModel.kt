package com.grappim.docuvault.feature.docgroup.manager

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.docuvault.feature.docgroup.domain.GroupToCreate
import com.grappim.docuvault.feature.docgroup.repoapi.GroupRepository
import com.grappim.docuvault.uikit.theme.Dom_Aero
import com.grappim.docuvault.uikit.utils.ColorUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupManagerViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val colorUtils: ColorUtils
) : ViewModel() {

    private val _viewState = MutableStateFlow(
        GroupManagerState(
            color = Dom_Aero,
            setName = ::setName,
            setColor = ::setColor,
            onGroupDone = ::createGroup
        )
    )

    val viewState = _viewState.asStateFlow()

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
