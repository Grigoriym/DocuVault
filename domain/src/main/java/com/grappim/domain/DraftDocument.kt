package com.grappim.domain

import java.time.OffsetDateTime

data class DraftDocument(
    val id: Long,
    val date: OffsetDateTime,
    val folderName: String
)
