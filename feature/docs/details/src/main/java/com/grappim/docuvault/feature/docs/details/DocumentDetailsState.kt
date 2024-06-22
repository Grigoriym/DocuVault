package com.grappim.docuvault.feature.docs.details

import com.grappim.docuvault.feature.docs.domain.Document
import com.grappim.docuvault.feature.docs.uiapi.DocumentFileUI

data class DocumentDetailsState(
    val document: Document? = null,
    val files: List<DocumentFileUI> = emptyList(),

    val updateProduct: () -> Unit
)
