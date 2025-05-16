package com.grappim.docuvault.feature.docs.domain

import com.grappim.docuvault.feature.docgroup.domain.Group
import java.time.OffsetDateTime

data class DraftDocument(
    val id: Long,
    val date: OffsetDateTime,
    val documentFolderName: String,
    val group: Group
)
