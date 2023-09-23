package com.grappim.docsofmine.gdrive

import java.time.OffsetDateTime

/**
 * https://developers.google.com/drive/api/v3/reference/files
 */
data class GDriveFileWrapper(
    val gDriveFileId: String,
    val name: String,
    val mimeType: String?,

    val createdTime: OffsetDateTime?,
    val modifiedTime: OffsetDateTime?,

    val appProperties: Map<String, String>? = null,
    val parents: List<String>? = null,
)
