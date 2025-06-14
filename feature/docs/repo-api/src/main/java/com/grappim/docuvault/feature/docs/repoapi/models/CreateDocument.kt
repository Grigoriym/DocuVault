package com.grappim.docuvault.feature.docs.repoapi.models

import com.grappim.docuvault.feature.docgroup.repoapi.model.Group
import java.time.OffsetDateTime

/**
 * A data class which is used when we create a new document
 */
data class CreateDocument(
    val id: Long,
    val name: String,
    val description: String,
    val group: Group,
    val files: List<DocumentFile>,
    val createdDate: OffsetDateTime,
    val documentFolderName: String
)
