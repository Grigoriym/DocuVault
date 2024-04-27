package com.grappim.docuvault.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grappim.docuvault.data.db.converters.DateTimeConverter
import com.grappim.docuvault.data.db.dao.DocumentsDao
import com.grappim.docuvault.data.db.dao.GroupsDao
import com.grappim.docuvault.data.db.model.document.DocumentEntity
import com.grappim.docuvault.data.db.model.document.DocumentFileDataEntity
import com.grappim.docuvault.data.db.model.group.GroupEntity
import com.grappim.docuvault.data.db.model.group.GroupFieldEntity

@Database(
    entities = [
        GroupEntity::class,
        GroupFieldEntity::class,
        DocumentEntity::class,
        DocumentFileDataEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    DateTimeConverter::class
)
abstract class DocuVaultDatabase : RoomDatabase() {
    abstract fun categoryDao(): GroupsDao
    abstract fun documentsDao(): DocumentsDao
}
