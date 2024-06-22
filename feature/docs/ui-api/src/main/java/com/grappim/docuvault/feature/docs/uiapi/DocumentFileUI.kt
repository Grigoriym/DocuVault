package com.grappim.docuvault.feature.docs.uiapi

import android.net.Uri

data class DocumentFileUI(
    val fileId: Long = 0L,
    val uri: Uri,
    val name: String,
    val size: Long,
    val mimeType: String,
    val md5: String,
    val isEdit: Boolean = false
)
