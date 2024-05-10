package com.grappim.docuvault.feature.group.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "group_table"
)
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    val groupId: Long = 0,
    val name: String,
    val color: String
)
