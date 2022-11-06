package com.grappim.docsofmine.data.db.model

@kotlinx.serialization.Serializable
data class DocumentFileUriDTO(
    val fileName: String,
    val mimeType: String,
    val path: String,
    val string: String,
    val fileSize: String,
)
