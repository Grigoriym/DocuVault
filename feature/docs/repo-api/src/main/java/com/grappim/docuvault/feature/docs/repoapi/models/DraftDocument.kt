package com.grappim.docuvault.feature.docs.repoapi.models

import com.grappim.docuvault.feature.docgroup.repoapi.model.Group
import java.time.OffsetDateTime

data class DraftDocument(
    val id: Long,
    val date: OffsetDateTime,
    val documentFolderName: String,
    val group: Group
)
