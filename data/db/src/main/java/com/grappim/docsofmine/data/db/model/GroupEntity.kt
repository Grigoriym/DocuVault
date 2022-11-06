package com.grappim.docsofmine.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "group_table"
)
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val fields: List<GroupFieldEntity>,
    val color: String
)
