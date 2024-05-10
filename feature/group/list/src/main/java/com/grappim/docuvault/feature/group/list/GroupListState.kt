package com.grappim.docuvault.feature.group.list

import com.grappim.docuvault.feature.group.domain.Group

data class GroupListState(
    val groups: List<Group> = emptyList()
)
