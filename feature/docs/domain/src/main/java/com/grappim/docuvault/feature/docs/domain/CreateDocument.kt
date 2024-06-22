package com.grappim.docuvault.feature.docs.domain

import com.grappim.docuvault.feature.group.domain.Group
import java.time.OffsetDateTime

data class CreateDocument(
    val id: Long,
    val name: String,
    val group: Group,
    val files: List<DocumentFile>,
    val createdDate: OffsetDateTime,
    val documentFolderName: String
)
