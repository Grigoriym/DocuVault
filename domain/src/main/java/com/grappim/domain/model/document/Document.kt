package com.grappim.domain.model.document

import com.grappim.docuvault.feature.group.domain.Group
import java.time.OffsetDateTime

data class Document(
    val documentId: Long,
    val name: String,
    val createdDate: OffsetDateTime,
    val group: Group,
    val filesUri: List<DocumentFileData>,
    val documentFolderName: String,
    val isCreated: Boolean
)
