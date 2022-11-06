package com.grappim.docsofmine.utils.files

import android.net.Uri

data class FileUris(
    val fileUri: Uri,
    val fileName: String,
    val fileSize: String,
    val mimeType: String,
    val filePreviewUri: Uri? = null
)
