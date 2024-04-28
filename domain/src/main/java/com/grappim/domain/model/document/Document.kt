package com.grappim.domain.model.document

import com.grappim.domain.model.group.Group
import java.time.OffsetDateTime

data class Document(
    val id: Long,
    val name: String,
    val group: Group,
    val filesUri: List<DocumentFileData>,
    val createdDate: OffsetDateTime,
    val isSynced: Boolean = false
) {
    companion object {
        fun getForPreview(): Document = Document(
            id = 1,
            name = "Doc",
            group = Group(id = 0, name = "group", fields = listOf(), color = "98D9C2"),
            filesUri = listOf(),
            createdDate = OffsetDateTime.now()
        )
    }
}
