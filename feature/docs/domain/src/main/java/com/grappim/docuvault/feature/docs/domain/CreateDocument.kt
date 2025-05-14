package com.grappim.docuvault.feature.docs.domain

import com.grappim.docuvault.feature.docgroup.domain.Group
import java.time.OffsetDateTime

data class CreateDocument(
    val id: Long,
    val name: String,
    val group: Group,
    val files: List<DocumentFile>,
    val createdDate: OffsetDateTime,
    val documentFolderName: String
)
