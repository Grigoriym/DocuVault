package com.grappim.docuvault.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grappim.docuvault.data.backupdb.BackupDocumentFileEntity
import com.grappim.docuvault.data.backupdb.BackupFilesDao
import com.grappim.docuvault.data.db.converters.DateTimeConverter
import com.grappim.docuvault.feature.docgroup.db.dao.GroupsDao
import com.grappim.docuvault.feature.docgroup.db.model.GroupEntity
import com.grappim.docuvault.feature.docgroup.db.model.GroupFieldEntity
import com.grappim.docuvault.feature.docs.db.dao.DocumentsDao
import com.grappim.docuvault.feature.docs.db.model.DocumentEntity
import com.grappim.docuvault.feature.docs.db.model.DocumentFileEntity

@Database(
    entities = [
        GroupEntity::class,
        GroupFieldEntity::class,
        DocumentEntity::class,
        DocumentFileEntity::class,
        BackupDocumentFileEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    DateTimeConverter::class
)
abstract class DocuVaultDatabase : RoomDatabase() {
    abstract fun groupsDao(): GroupsDao
    abstract fun documentsDao(): DocumentsDao

    abstract fun backupFilesDao(): BackupFilesDao
}
