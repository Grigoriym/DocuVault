package com.grappim.domain.model.document

data class DocumentFileData(
    val fileId: Long,
    val name: String,
    val mimeType: String,
    val uriString: String,
    val size: Long,
    val md5: String
)
