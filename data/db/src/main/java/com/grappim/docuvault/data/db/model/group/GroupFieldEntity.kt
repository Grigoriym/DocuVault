package com.grappim.docuvault.data.db.model.group

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "group_field_table"
)
data class GroupFieldEntity(
    @PrimaryKey(autoGenerate = true)
    val groupId: Long = 0,
    val name: String,
    val value: String
)
