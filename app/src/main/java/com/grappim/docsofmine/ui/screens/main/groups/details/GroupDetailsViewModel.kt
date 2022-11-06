package com.grappim.docsofmine.ui.screens.main.groups.details

import androidx.lifecycle.ViewModel
import com.grappim.domain.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupDetailsViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {
}