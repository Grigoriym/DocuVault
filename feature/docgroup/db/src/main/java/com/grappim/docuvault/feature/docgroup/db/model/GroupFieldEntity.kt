package com.grappim.docuvault.feature.docgroup.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "group_field_table",
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = arrayOf("groupId"),
            childColumns = arrayOf("groupId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index("groupId")]
)
data class GroupFieldEntity(
    @PrimaryKey(autoGenerate = true)
    val groupFieldId: Long = 0,
    val name: String,
    val value: String,
    val groupId: Long
)
