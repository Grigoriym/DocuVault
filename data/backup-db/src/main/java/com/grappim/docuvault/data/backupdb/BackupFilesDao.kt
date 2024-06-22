package com.grappim.docuvault.data.backupdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BackupFilesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<BackupDocumentFileEntity>)

    @Delete
    suspend fun delete(list: List<BackupDocumentFileEntity>)

    @Query("DELETE FROM backup_document_file_entity WHERE documentId =:documentId")
    suspend fun deleteFilesByDocumentId(documentId: Long)

    @Query("SELECT * FROM backup_document_file_entity WHERE documentId =:documentId")
    suspend fun getAllFilesByDocumentId(documentId: Long): List<BackupDocumentFileEntity>
}
