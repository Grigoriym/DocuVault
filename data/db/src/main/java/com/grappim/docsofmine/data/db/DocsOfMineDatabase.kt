package com.grappim.docsofmine.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grappim.docsofmine.data.db.converters.BaseListsConverter
import com.grappim.docsofmine.data.db.converters.DateTimeConverter
import com.grappim.docsofmine.data.db.converters.GroupFieldConverter
import com.grappim.docsofmine.data.db.dao.DocumentsDao
import com.grappim.docsofmine.data.db.dao.GroupsDao
import com.grappim.docsofmine.data.db.model.document.DocumentEntity
import com.grappim.docsofmine.data.db.model.document.DocumentFileDataEntity
import com.grappim.docsofmine.data.db.model.group.GroupEntity

@[Database(
    entities = [
        GroupEntity::class,
        DocumentEntity::class,
        DocumentFileDataEntity::class
    ],
    version = 1,
    exportSchema = true
)
TypeConverters(
    GroupFieldConverter::class,
    BaseListsConverter::class,
    DateTimeConverter::class
)]
abstract class DocsOfMineDatabase : RoomDatabase() {
    abstract fun categoryDao(): GroupsDao
    abstract fun documentsDao(): DocumentsDao
}