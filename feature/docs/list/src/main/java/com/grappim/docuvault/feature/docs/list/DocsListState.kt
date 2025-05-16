package com.grappim.docuvault.feature.docs.list

import com.grappim.docuvault.feature.docs.uiapi.DocumentListUI

data class DocsListState(
    val documents: List<DocumentListUI> = emptyList()
)
