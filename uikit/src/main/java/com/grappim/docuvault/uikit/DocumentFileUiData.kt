package com.grappim.docuvault.uikit

import android.net.Uri

data class DocumentFileUiData(
    val fileId: Long = 0L,
    val uri: Uri,
    val name: String,
    val size: Long,
    val mimeType: String,
    val md5: String,
    val isEdit: Boolean = false
)
