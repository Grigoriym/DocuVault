package com.grappim.docuvault.feature.docgroup.manager

import androidx.compose.ui.graphics.Color

data class GroupManagerState(
    val name: String = "",
    val color: Color,

    val setName: (String) -> Unit,
    val setColor: (Color) -> Unit,

    val onGroupDone: () -> Unit,

    val groupSaved: Boolean = false
)
