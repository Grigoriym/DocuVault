package com.grappim.docsofmine.gdrive

import android.net.Uri
import java.io.File
import java.time.OffsetDateTime

data class GDriveFileHolder(
    val name: String,
    val id: String,
    var size: Long,
    val mimeType: String?,
    val createdTime: OffsetDateTime?,
    val modifiedTime: OffsetDateTime?,
    val starred: Boolean,
    val appProperties: Map<String, String>? = null,
    val parents: List<String>? = null,

    val appFileId: Long,

    var file: File? = null,
    var fileUri: Uri? = null,

    var files: List<GDriveFileHolder> = emptyList(),

    var filePreviewUriString: String? = null,
    var filePreviewUriPath: String? = null
)
