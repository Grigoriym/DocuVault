package com.grappim.docsofmine.utils.states

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface CreateGroupStateViewModel {
    val createGroupInputState: StateFlow<CreateGroupInputState>
    fun setNewGroupState(createGroupInputState: CreateGroupInputState)
}

class CreateGroupStateViewModelImpl : CreateGroupStateViewModel {
    private val _createGroupInputState =
        MutableStateFlow<CreateGroupInputState>(CreateGroupInputStateImpl())
    override val createGroupInputState: StateFlow<CreateGroupInputState>
        get() = _createGroupInputState.asStateFlow()

    override fun setNewGroupState(createGroupInputState: CreateGroupInputState) {
        _createGroupInputState.value = createGroupInputState
    }
}