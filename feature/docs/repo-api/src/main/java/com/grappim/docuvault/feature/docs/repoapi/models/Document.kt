package com.grappim.docuvault.feature.docs.repoapi.models

import com.grappim.docuvault.feature.docgroup.repoapi.model.Group
import java.time.OffsetDateTime

data class Document(
    val documentId: Long,
    val name: String,
    val description: String = "",
    val createdDate: OffsetDateTime,
    val group: Group,
    val files: List<DocumentFile>,
    val documentFolderName: String
)
