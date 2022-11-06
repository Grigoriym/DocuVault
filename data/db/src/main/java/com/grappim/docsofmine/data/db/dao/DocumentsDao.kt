package com.grappim.docsofmine.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.grappim.docsofmine.data.db.model.DocumentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(documentEntity: DocumentEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(documentEntity: DocumentEntity)

    @Query("DELETE FROM document_table WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(documentEntity: DocumentEntity): Long

    @Query("SELECT * FROM document_table WHERE filesUri IS NOT NULL AND filesUri != ''")
    fun getAllFlow(): Flow<List<DocumentEntity>>

    @Query("SELECT * FROM document_table WHERE filesUri IS NOT NULL AND filesUri != ''")
    suspend fun getAll(): List<DocumentEntity>

    @Query("SELECT * FROM document_table WHERE filesUri IS NOT NULL AND filesUri != '' AND isSynced = 0")
    suspend fun getAllUnSynced(): List<DocumentEntity>

    @Query("UPDATE document_table SET isSynced = 1 WHERE isSynced = 0")
    suspend fun markAsSynced()

    @Query("SELECT * FROM document_table WHERE id=:id")
    fun getDocumentById(id: Long): Flow<DocumentEntity>
}