package com.grappim.domain.model.document

import java.time.OffsetDateTime

data class DraftDocument(
    val id: Long,
    val date: OffsetDateTime,
    val folderName: String
)
