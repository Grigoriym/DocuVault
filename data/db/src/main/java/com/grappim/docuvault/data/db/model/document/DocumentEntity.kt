package com.grappim.docuvault.data.db.model.document

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.grappim.docuvault.data.db.model.group.GroupEntity
import java.time.OffsetDateTime

@Entity(
    tableName = "document_table"
)
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true)
    val documentId: Long = 0,
    val name: String,
    @Embedded(
        prefix = "group"
    )
    val group: GroupEntity?,
    val createdDate: OffsetDateTime,
    val isSynced: Boolean = false
)
