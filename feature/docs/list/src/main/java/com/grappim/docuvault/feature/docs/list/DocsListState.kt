package com.grappim.docuvault.feature.docs.list

import com.grappim.docuvault.utils.files.models.DocumentListUI

data class DocsListState(
    val documents: List<DocumentListUI> = emptyList()
)
