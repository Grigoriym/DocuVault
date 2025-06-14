package com.grappim.docuvault.feature.docs.uiapi

import androidx.compose.ui.graphics.Color

data class DocumentListUI(
    val id: String,
    val name: String,
    val createdDate: String,
    val preview: List<String>,
    val groupColor: Color
)
