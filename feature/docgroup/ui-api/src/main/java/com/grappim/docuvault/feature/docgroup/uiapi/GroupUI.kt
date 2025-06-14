package com.grappim.docuvault.feature.docgroup.uiapi

import androidx.compose.ui.graphics.Color
import com.grappim.docuvault.feature.docgroup.repoapi.model.GroupField

data class GroupUI(
    val id: Long,
    val name: String,
    val fields: List<GroupField>,
    val color: Color,
    val colorString: String
)
