package com.grappim.docsofmine.gdrive

import java.time.OffsetDateTime

data class GDriveFileHolder(
    val name: String,
    val id: String,
    val size: Long,
    val mimeType: String?,
    val createdTime: OffsetDateTime?,
    val modifiedTime: OffsetDateTime?,
    val starred: Boolean,
    val appProperties: Map<String, String>? = null,
    val parents: List<String>? = null
)
