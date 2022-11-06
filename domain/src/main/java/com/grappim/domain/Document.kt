package com.grappim.domain

import java.time.OffsetDateTime

data class Document(
    val id: Long,
    val name: String,
    val group: Group,
    val filesUri: List<DocumentFileUri>,
    val createdDate: OffsetDateTime,
    val createdDateString: String
) {
    companion object {
        fun getForPreview(): Document =
            Document(
                id = 1,
                name = "Doc",
                group = Group(id = 0, name = "group", fields = listOf(), color = "98D9C2"),
                filesUri = listOf(),
                createdDate = OffsetDateTime.now(),
                createdDateString = "23 Oct 2023"
            )

    }

    fun getGDriveFileName(
        gDriveFormattedDate: String
    ): String =
        "${id}_${gDriveFormattedDate}"
}

data class CreateDocument(
    val id: Long,
    val name: String,
    val group: Group,
    val filesUri: List<DocumentFileUri>,
    val createdDate: OffsetDateTime
)

data class DocumentFileUri(
    val fileName: String,
    val mimeType: String,
    val path: String,
    val string: String,
    val size: String,
    val filePreviewUriString: String? = null,
    val filePreviewUriPath: String? = null
)
