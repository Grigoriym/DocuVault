package com.grappim.domain.model.document

import com.grappim.docuvault.feature.group.domain.Group
import java.time.OffsetDateTime

data class CreateDocument(
    val id: Long,
    val name: String,
    val group: Group,
    val files: List<DocumentFileData>,
    val createdDate: OffsetDateTime,
    val documentFolderName: String
)
