package com.grappim.docsofmine.data.db.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(
    tableName = "document_table"
)
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @Embedded(
        prefix = "group"
    )
    val group: GroupEntity?,
    val filesUri: List<DocumentFileUriDTO>,
    val createdDate: OffsetDateTime,
    val isSynced: Boolean = false
)
