package com.grappim.docuvault.feature.docs.repoapi.usecase

import com.grappim.docuvault.feature.docgroup.repoapi.model.Group
import com.grappim.docuvault.feature.docs.repoapi.models.DocumentFile
import java.time.OffsetDateTime

data class EditDocumentFinalizeData(
    val documentId: Long,
    val documentFolderName: String,
    val editedFiles: List<DocumentFile>,
    val documentName: String,
    val createdDate: OffsetDateTime,
    val group: Group
)
