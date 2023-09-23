package com.grappim.docsofmine.gdrive

import android.net.Uri
import java.io.File

data class LocalGDriveChildFile(
    val name: String,
    val mimeType: String,

    val md5: String,
    val size: Long,
    val file: File,
    val fileUri: Uri,

    val filePreviewUriString: String? = null,
    val filePreviewUriPath: String? = null
)
