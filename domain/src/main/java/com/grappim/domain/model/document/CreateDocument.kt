package com.grappim.domain.model.document

import com.grappim.domain.model.group.Group
import java.time.OffsetDateTime

data class CreateDocument(
    val id: Long,
    val name: String,
    val group: Group,
    val filesUri: List<DocumentFileData>,
    val createdDate: OffsetDateTime
)