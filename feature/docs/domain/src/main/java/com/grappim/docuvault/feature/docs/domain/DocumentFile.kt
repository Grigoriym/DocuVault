package com.grappim.docuvault.feature.docs.domain

data class DocumentFile(
    val fileId: Long,
    val name: String,
    val mimeType: String,
    val uriString: String,
    val size: Long,
    val md5: String,
    val isEdit: Boolean = false
)
