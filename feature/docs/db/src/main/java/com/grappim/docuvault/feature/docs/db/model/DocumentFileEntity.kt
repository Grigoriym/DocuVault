package com.grappim.docuvault.feature.docs.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "document_file_table",
    foreignKeys = [
        ForeignKey(
            entity = DocumentEntity::class,
            parentColumns = arrayOf("documentId"),
            childColumns = arrayOf("documentId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index("documentId")]
)
data class DocumentFileEntity(
    @PrimaryKey(autoGenerate = true)
    val fileId: Long = 0,

    val documentId: Long,

    val name: String,
    val mimeType: String,
    val size: Long,

    val uriString: String,

    val md5: String
)
