package com.grappim.docsofmine.data.repository

import com.grappim.docsofmine.common.async.IoDispatcher
import com.grappim.docsofmine.data.db.dao.DocumentsDao
import com.grappim.docsofmine.data.db.model.document.DocumentEntity
import com.grappim.docsofmine.data.mappers.toDocument
import com.grappim.docsofmine.data.mappers.toEntity
import com.grappim.docsofmine.data.mappers.toFileDataEntityList
import com.grappim.docsofmine.utils.dateTime.DateTimeUtils
import com.grappim.domain.model.document.CreateDocument
import com.grappim.domain.model.document.Document
import com.grappim.domain.model.document.DraftDocument
import com.grappim.domain.repository.DocumentRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DocumentRepositoryImpl @Inject constructor(
    private val documentsDao: DocumentsDao,
    private val dateTimeUtils: DateTimeUtils,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DocumentRepository {

    override suspend fun addDraftDocument(): DraftDocument {
        val nowDate = dateTimeUtils.getDateTimeUTCNow()
        val formattedDate = dateTimeUtils.formatToDemonstrate(nowDate)
        val id = documentsDao.insert(
            DocumentEntity(
                name = formattedDate,
                group = null,
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
                it.filter { entity ->
                    entity.files?.isNotEmpty() == true
                }
            }
            .map {
                it.map { documentWithFilesEntity ->
                    documentWithFilesEntity.documentEntity.toDocument(documentWithFilesEntity.files)
                }
            }

    override suspend fun markAsSynced(synced: List<Document>) = withContext(ioDispatcher) {
        synced.map {
            async {
                documentsDao.markAsSynced(it.id)
            }
        }.awaitAll()
        Unit
    }

    override suspend fun getAllDocuments(): List<Document> =
        documentsDao.getAll()
            .filter {
                it.files?.isNotEmpty() == true
            }
            .map { documentWithFilesEntity ->
                documentWithFilesEntity.documentEntity.toDocument(documentWithFilesEntity.files)
            }

    override suspend fun getAllUnSynced(): List<Document> =
        documentsDao.getAllUnSynced()
            .filter {
                it.files?.isNotEmpty() == true
            }
            .map { documentWithFilesEntity ->
                documentWithFilesEntity.documentEntity.toDocument(documentWithFilesEntity.files)
            }

    override suspend fun addDocument(document: CreateDocument) {
        val entity = document.toEntity()
        val list = document.toFileDataEntityList()
        documentsDao.updateDocumentAndFiles(
            documentEntity = entity,
            list = list
        )
    }

    override suspend fun addDocument(document: Document) {
        documentsDao.insert(document.toEntity())
    }

    override suspend fun addDocuments(
        documents: List<Document>
    ) = withContext(ioDispatcher) {
        documents.map { document ->
            async {
                documentsDao.insertDocumentAndFiles(
                    documentEntity = document.toEntity(),
                    list = document.toFileDataEntityList()
                )
            }
        }.awaitAll()
        Unit
    }

    override suspend fun removeDocumentById(id: Long) {
        documentsDao.deleteById(id)
    }

    override fun getDocumentById(id: Long): Flow<Document> =
        documentsDao.getDocumentById(id)
            .map { documentWithFilesEntity ->
                documentWithFilesEntity.documentEntity.toDocument(documentWithFilesEntity.files)
            }

}