package com.grappim.docsofmine.data.model.document

@kotlinx.serialization.Serializable
data class DocumentFileUriDTO(
    val name: String,
    val mimeType: String,
    val path: String,
    val string: String,
    val size: Long,
    val previewUriString: String? = null,
    val previewUriPath: String? = null
)
