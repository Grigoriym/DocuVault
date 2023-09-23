package com.grappim.docsofmine.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.grappim.docsofmine.data.db.model.document.DocumentEntity
import com.grappim.docsofmine.data.db.model.document.DocumentFileDataEntity
import com.grappim.docsofmine.data.db.model.document.DocumentWithFilesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentsDao {

    @Transaction
    suspend fun updateDocumentAndFiles(
        documentEntity: DocumentEntity,
        list: List<DocumentFileDataEntity>
    ) {
        update(documentEntity)
        insertFiles(list)
    }

    @Transaction
    suspend fun insertDocumentAndFiles(
        documentEntity: DocumentEntity,
        list: List<DocumentFileDataEntity>
    ) {
        insert(documentEntity)
        insertFiles(list)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(documentEntity: DocumentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(documentEntities: List<DocumentEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(documentEntity: DocumentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFiles(list: List<DocumentFileDataEntity>)

    @Query("DELETE FROM document_table WHERE documentId = :id")
    suspend fun deleteById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(documentEntity: DocumentEntity): Long

    @Query("SELECT * FROM document_table")
    @Transaction
    fun getAllFlow(): Flow<List<DocumentWithFilesEntity>>

    @Query("SELECT * FROM document_table")
    @Transaction
    suspend fun getAll(): List<DocumentWithFilesEntity>

    @Query("SELECT * FROM document_table WHERE isSynced = 0")
    suspend fun getAllUnSynced(): List<DocumentWithFilesEntity>

    @Query("UPDATE document_table SET isSynced = 1 WHERE documentId=:id AND isSynced = 0")
    suspend fun markAsSynced(id: Long)

    @Query("SELECT * FROM document_table WHERE documentId=:id")
    fun getDocumentById(id: Long): Flow<DocumentWithFilesEntity>
}