package com.grappim.docuvault.feature.docgroup.list

import com.grappim.docuvault.feature.docgroup.uiapi.GroupUI

data class GroupListState(
    val groups: List<GroupUI> = emptyList()
)
