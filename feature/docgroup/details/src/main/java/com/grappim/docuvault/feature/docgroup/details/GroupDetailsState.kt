package com.grappim.docuvault.feature.docgroup.details

import com.grappim.docuvault.feature.docgroup.domain.Group
import com.grappim.docuvault.feature.docs.uiapi.DocumentListUI

data class GroupDetailsState(
    val group: Group? = null,
    val documents: List<DocumentListUI> = emptyList()
)
