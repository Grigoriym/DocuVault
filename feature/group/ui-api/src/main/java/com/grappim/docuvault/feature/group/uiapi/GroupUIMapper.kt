package com.grappim.docuvault.feature.group.uiapi

import com.grappim.docuvault.feature.group.domain.Group

interface GroupUIMapper {
    suspend fun toGroupUIList(list: List<Group>): List<GroupUI>
    suspend fun toGroupUI(group: Group): GroupUI

    suspend fun toGroup(groupUI: GroupUI): Group
}
