package com.grappim.docsofmine.model.document

import androidx.compose.ui.graphics.Color

data class DocumentListUI(
    val id: String,
    val name: String,
    val createdDate: String,
    val preview: Any,
    val groupColor: Color
)
