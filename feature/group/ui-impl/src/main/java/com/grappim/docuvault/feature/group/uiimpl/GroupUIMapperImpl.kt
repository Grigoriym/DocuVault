package com.grappim.docuvault.feature.group.uiimpl

import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.feature.group.domain.Group
import com.grappim.docuvault.feature.group.uiapi.GroupUI
import com.grappim.docuvault.feature.group.uiapi.GroupUIMapper
import com.grappim.docuvault.uikit.utils.ColorUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupUIMapperImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val colorUtils: ColorUtils
) : GroupUIMapper {
    override suspend fun toGroupUIList(list: List<Group>): List<GroupUI> =
        list.map { toGroupUI(it) }

    override suspend fun toGroupUI(group: Group): GroupUI = withContext(ioDispatcher) {
        GroupUI(
            id = group.id,
            name = group.name,
            fields = group.fields,
            color = colorUtils.toComposeColor(group.color),
            colorString = group.color
        )
    }

    override suspend fun toGroup(groupUI: GroupUI): Group = withContext(ioDispatcher) {
        Group(
            id = groupUI.id,
            name = groupUI.name,
            fields = groupUI.fields,
            color = groupUI.colorString
        )
    }
}
