package com.grappim.docsofmine.gdrive

import android.net.Uri
import com.grappim.domain.model.group.Group
import java.io.File
import java.time.OffsetDateTime

/**
 * It is an actual json file from which it is easy to download all document files
 */
data class LocalGDriveParentFile(
    val id: Long,
    val group: Group,
    val name: String,
    val mimeType: String,

    val children: List<LocalGDriveChildFile>,
    val md5: String,
    val size: Long,
    val file: File,
    val fileUri: Uri,

    val appProperties: Map<String, String>? = null,

    val createdTime: OffsetDateTime?,
    val modifiedTime: OffsetDateTime?
)
