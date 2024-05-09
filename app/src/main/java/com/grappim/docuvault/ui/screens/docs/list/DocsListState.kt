package com.grappim.docuvault.ui.screens.docs.list

import com.grappim.docuvault.utils.files.models.DocumentListUI

data class DocsListState(
    val documents: List<DocumentListUI> = emptyList()
)
