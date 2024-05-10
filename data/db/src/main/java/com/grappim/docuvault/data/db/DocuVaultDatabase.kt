package com.grappim.docuvault.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grappim.docuvault.data.db.converters.DateTimeConverter
import com.grappim.docuvault.data.db.dao.DocumentsDao
import com.grappim.docuvault.data.db.model.document.DocumentEntity
import com.grappim.docuvault.data.db.model.document.DocumentFileEntity
import com.grappim.docuvault.feature.group.db.model.GroupEntity
import com.grappim.docuvault.feature.group.db.model.GroupFieldEntity

@Database(
    entities = [
        GroupEntity::class,
        GroupFieldEntity::class,
        DocumentEntity::class,
        DocumentFileEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    DateTimeConverter::class
)
abstract class DocuVaultDatabase : RoomDatabase() {
    abstract fun categoryDao(): com.grappim.docuvault.feature.group.db.dao.GroupsDao
    abstract fun documentsDao(): DocumentsDao
}
