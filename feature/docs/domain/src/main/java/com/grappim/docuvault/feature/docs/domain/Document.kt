package com.grappim.docuvault.feature.docs.domain

import com.grappim.docuvault.feature.docgroup.domain.Group
import java.time.OffsetDateTime

data class Document(
    val documentId: Long,
    val name: String,
    val createdDate: OffsetDateTime,
    val group: Group,
    val files: List<DocumentFile>,
    val documentFolderName: String
)
