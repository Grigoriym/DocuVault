package com.grappim.docuvault.feature.docs.details

import android.content.Intent
import com.grappim.docuvault.feature.docs.repoapi.models.Document
import com.grappim.docuvault.feature.docs.uiapi.DocumentFileUI

data class DocumentDetailsState(
    val document: Document? = null,
    val files: List<DocumentFileUI> = emptyList(),

    val updateProduct: () -> Unit,
    val onFileClicked: (DocumentFileUI) -> Unit = {},
    val openImageIntent: Intent? = null
)
