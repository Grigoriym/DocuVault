package com.grappim.docuvault.feature.group.list

import com.grappim.docuvault.feature.group.uiapi.GroupUI

data class GroupListState(
    val groups: List<GroupUI> = emptyList()
)
