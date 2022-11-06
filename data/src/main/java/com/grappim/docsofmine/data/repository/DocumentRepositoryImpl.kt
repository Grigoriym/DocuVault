package com.grappim.docsofmine.data.repository

import com.grappim.docsofmine.data.db.dao.DocumentsDao
import com.grappim.docsofmine.data.db.model.DocumentEntity
import com.grappim.docsofmine.data.mappers.toDocument
import com.grappim.docsofmine.data.mappers.toEntity
import com.grappim.docsofmine.utils.datetime.DateTimeUtils
import com.grappim.domain.CreateDocument
import com.grappim.domain.Document
import com.grappim.domain.DraftDocument
import com.grappim.domain.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DocumentRepositoryImpl @Inject constructor(
    private val documentsDao: DocumentsDao,
    private val dateTimeUtils: DateTimeUtils
) : DocumentRepository {

    override suspend fun addDraftDocument(): DraftDocument {
        val nowDate = OffsetDateTime.now()
        val formattedDate = dateTimeUtils.formatToDemonstrate(nowDate)
        val id = documentsDao.insert(
            DocumentEntity(
                name = formattedDate,
                group = null,
                filesUri = emptyList(),
                createdDate = nowDate
            )
        )
        val folderDate = dateTimeUtils.formatToGDrive(nowDate)
        val folderName = "${id}_${folderDate}"
        return DraftDocument(
            id = id,
            date = nowDate,
            folderName = folderName
        )
    }

    override fun getAllDocumentsFlow(): Flow<List<Document>> =
        documentsDao.getAllFlow()
            .map {
                it.map { entity ->
                        val createdDateString =
                            dateTimeUtils.formatToDemonstrate(entity.createdDate)
                        entity.toDocument(createdDateString)
                    }
            }

    override suspend fun markAsSynced() {
        documentsDao.markAsSynced()
    }

    override suspend fun getAllDocuments(): List<Document> =
        documentsDao.getAll().map { entity ->
            val createdDateString = dateTimeUtils.formatToDemonstrate(entity.createdDate)
            entity.toDocument(createdDateString)
        }

    override suspend fun getAllUnSynced(): List<Document> =
        documentsDao.getAllUnSynced().map { entity ->
            val createdDateString = dateTimeUtils.formatToDemonstrate(entity.createdDate)
            entity.toDocument(createdDateString)
        }

    override suspend fun addDocument(document: CreateDocument) {
        documentsDao.update(
            document.toEntity()
        )
    }

    override suspend fun removeDocumentById(id: Long) {
        documentsDao.deleteById(id)
    }

    override fun getDocumentById(id: Long): Flow<Document> =
        documentsDao.getDocumentById(id)
            .map { entity ->
                val createdDateString = dateTimeUtils.formatToDemonstrate(entity.createdDate)
                entity.toDocument(createdDateString)
            }

}