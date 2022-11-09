package com.grappim.domain.model.document

data class DocumentFileData(
    val name: String,
    val mimeType: String,
    val uriPath: String,
    val uriString: String,
    val size: Long,
    val previewUriString: String? = null,
    val previewUriPath: String? = null
)
