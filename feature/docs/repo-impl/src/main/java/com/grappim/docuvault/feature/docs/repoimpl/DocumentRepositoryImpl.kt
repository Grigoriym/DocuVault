package com.grappim.docuvault.feature.docs.repoimpl

import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.data.dbapi.DatabaseWrapper
import com.grappim.docuvault.feature.docgroup.repoapi.mappers.GroupMapper
import com.grappim.docuvault.feature.docs.db.model.DocumentEntity
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.feature.docs.repoapi.mappers.DocumentFileMapper
import com.grappim.docuvault.feature.docs.repoapi.mappers.DocumentMapper
import com.grappim.docuvault.feature.docs.repoapi.models.CreateDocument
import com.grappim.docuvault.feature.docs.repoapi.models.Document
import com.grappim.docuvault.feature.docs.repoapi.models.DocumentFile
import com.grappim.docuvault.feature.docs.repoapi.models.DraftDocument
import com.grappim.docuvault.utils.datetimeapi.DateTimeUtils
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
    private val dateTimeUtils: DateTimeUtils,
    private val documentMapper: DocumentMapper,
    private val databaseWrapper: DatabaseWrapper,
    private val groupMapper: GroupMapper,
    private val documentFileMapper: DocumentFileMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DocumentRepository {

    override suspend fun getDocumentById(id: Long): Document =
        documentMapper.toDocument(databaseWrapper.documentsDao.getFullDocument(id))

    override suspend fun deleteDocumentFile(documentId: Long, fileName: String) {
        databaseWrapper.documentsDao.deleteDocumentFileByIdAndName(documentId, fileName)
    }

    override suspend fun deleteDocumentById(documentId: Long) {
        databaseWrapper.documentsDao.deleteDocumentAndFilesById(documentId)
    }

    override suspend fun addDraftDocument(): DraftDocument = withContext(ioDispatcher) {
        val defaultGroup = databaseWrapper.groupsDao.getFirstGroup()
        val nowDate = dateTimeUtils.getDateTimeUTCNow()
        val folderDate = dateTimeUtils.formatToDocumentFolder(nowDate)
        val id = databaseWrapper.documentsDao.insert(
            DocumentEntity(
                name = "",
                documentFolderName = "",
                createdDate = nowDate,
                groupId = defaultGroup.groupEntity.groupId
            )
        )
        val folderName = "${id}_$folderDate"
        databaseWrapper.documentsDao.updateProductFolderName(folderName, id)
        DraftDocument(
            id = id,
            date = nowDate,
            documentFolderName = folderName,
            group = groupMapper.toGroup(defaultGroup)
        )
    }

    override fun getAllDocumentsFlow(): Flow<List<Document>> =
        databaseWrapper.documentsDao.getFullDocumentsFlow()
            .map { list -> documentMapper.toDocumentList(list) }

    override suspend fun addDocument(createDocument: CreateDocument) {
        val entity = documentMapper.toDocumentEntity(createDocument)
        val list = documentFileMapper.toFileDataEntity(createDocument)
        databaseWrapper.documentsDao.updateDocumentAndFiles(
            documentEntity = entity,
            list = list
        )
    }

    override suspend fun addDocument(document: Document) {
        databaseWrapper.documentsDao.insert(documentMapper.toDocumentEntity(document))
    }

    override suspend fun addDocuments(documents: List<Document>): Unit = withContext(ioDispatcher) {
        documents.map { document ->
            async {
                databaseWrapper.documentsDao.insertDocumentAndFiles(
                    documentEntity = documentMapper.toDocumentEntity(document),
                    list = documentFileMapper.toDocumentFileEntityList(
                        documentId = document.documentId,
                        files = document.files
                    )
                )
            }
        }.awaitAll()
    }

    override suspend fun removeDocumentById(id: Long) {
        databaseWrapper.documentsDao.deleteDocumentById(id)
    }

    override suspend fun updateDocumentWithFiles(document: Document, files: List<DocumentFile>) {
        val entity = documentMapper.toDocumentEntity(document)
        val filesEntities = documentFileMapper.toDocumentFileEntityList(
            documentId = document.documentId,
            files = files
        )
        databaseWrapper.documentsDao.updateDocumentAndFiles(entity, filesEntities)
    }

    override suspend fun updateFilesInDocument(documentId: Long, files: List<DocumentFile>) {
        val filesEntities = documentFileMapper.toDocumentFileEntityList(documentId, files)
        databaseWrapper.documentsDao.upsertFiles(filesEntities)
    }

    override suspend fun getDocumentsByGroupId(groupId: Long): List<Document> {
        val entities = databaseWrapper.documentsDao.getDocumentsByGroupId(groupId)
        return documentMapper.toDocumentList(entities)
    }
}
