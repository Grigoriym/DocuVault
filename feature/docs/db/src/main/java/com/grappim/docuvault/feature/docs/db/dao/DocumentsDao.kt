package com.grappim.docuvault.feature.docs.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.grappim.docuvault.feature.docs.db.model.DocumentEntity
import com.grappim.docuvault.feature.docs.db.model.DocumentFileEntity
import com.grappim.docuvault.feature.docs.db.model.FullDocumentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentsDao {

    @[Transaction Query("SELECT * FROM document_table WHERE isCreated=1 ORDER BY createdDate")]
    fun getFullDocumentsFlow(): Flow<List<FullDocumentEntity>>

    @Query("DELETE FROM document_file_table WHERE documentId = :documentId AND name=:fileName")
    suspend fun deleteDocumentFileByIdAndName(documentId: Long, fileName: String)

    @[Transaction Query("SELECT * FROM document_table WHERE documentId=:documentId LIMIT 1")]
    suspend fun getFullDocument(documentId: Long): FullDocumentEntity

    @Query("UPDATE document_table SET documentFolderName=:folder WHERE documentId=:documentId")
    suspend fun updateProductFolderName(folder: String, documentId: Long)

    @Query("DELETE FROM document_table WHERE documentId = :id")
    suspend fun deleteDocumentById(id: Long)

    @Query("DELETE FROM document_file_table WHERE documentId = :documentId")
    suspend fun deleteFilesByDocumentId(documentId: Long)

    @Transaction
    suspend fun deleteDocumentAndFilesById(documentId: Long) {
        deleteDocumentById(documentId)
        deleteFilesByDocumentId(documentId)
    }

    @Transaction
    suspend fun updateDocumentAndFiles(
        documentEntity: DocumentEntity,
        list: List<DocumentFileEntity>
    ) {
        update(documentEntity)
        insertFiles(list)
    }

    @Transaction
    suspend fun insertDocumentAndFiles(
        documentEntity: DocumentEntity,
        list: List<DocumentFileEntity>
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
    suspend fun insertFiles(list: List<DocumentFileEntity>)

    @Upsert
    suspend fun upsertFiles(list: List<DocumentFileEntity>)
}
