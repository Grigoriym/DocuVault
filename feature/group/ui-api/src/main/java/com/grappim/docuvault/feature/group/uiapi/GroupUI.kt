package com.grappim.docuvault.feature.group.uiapi

import androidx.compose.ui.graphics.Color
import com.grappim.docuvault.feature.group.domain.GroupField

data class GroupUI(
    val id: Long,
    val name: String,
    val fields: List<GroupField>,
    val color: Color,
    val colorString: String
)
