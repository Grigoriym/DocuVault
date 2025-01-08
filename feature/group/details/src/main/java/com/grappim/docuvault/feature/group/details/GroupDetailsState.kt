package com.grappim.docuvault.feature.group.details

import com.grappim.docuvault.feature.group.domain.Group
import com.grappim.docuvault.utils.files.models.DocumentListUI

data class GroupDetailsState(
    val group: Group? = null,
    val documents: List<DocumentListUI> = emptyList()
)
