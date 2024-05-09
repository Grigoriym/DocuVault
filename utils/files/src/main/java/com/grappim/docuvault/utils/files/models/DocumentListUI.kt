package com.grappim.docuvault.utils.files.models

import androidx.compose.ui.graphics.Color

data class DocumentListUI(
    val id: String,
    val name: String,
    val createdDate: String,
    val preview: String,
    val groupColor: Color
)
