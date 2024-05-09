package com.grappim.domain.model.document

import com.grappim.domain.model.group.Group
import java.time.OffsetDateTime

data class DraftDocument(
    val id: Long,
    val date: OffsetDateTime,
    val documentFolderName: String,
    val group: Group
)
