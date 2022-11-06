package com.grappim.docsofmine.data.model

import com.grappim.docsofmine.data.db.model.DocumentFileUriDTO
import com.grappim.docsofmine.data.serializers.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@kotlinx.serialization.Serializable
data class DocumentDTO(
    val id: Long,
    val name: String,
    val group: GroupDTO,
    val filesUri: List<DocumentFileUriDTO>,
    @kotlinx.serialization.Serializable(OffsetDateTimeSerializer::class)
    val createdDate: OffsetDateTime
)