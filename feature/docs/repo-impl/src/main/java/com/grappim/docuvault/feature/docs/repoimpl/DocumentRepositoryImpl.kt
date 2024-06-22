package com.grappim.docuvault.feature.docs.repoimpl

import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.datetime.DateTimeUtils
import com.grappim.docuvault.feature.docs.db.dao.DocumentsDao
import com.grappim.docuvault.feature.docs.db.model.DocumentEntity
import com.grappim.docuvault.feature.docs.domain.CreateDocument
import com.grappim.docuvault.feature.docs.domain.Document
import com.grappim.docuvault.feature.docs.domain.DocumentFile
import com.grappim.docuvault.feature.docs.domain.DraftDocument
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.feature.docs.repoapi.mappers.DocumentFileMapper
import com.grappim.docuvault.feature.docs.repoapi.mappers.DocumentMapper
import com.grappim.docuvault.feature.group.db.dao.GroupsDao
import com.grappim.docuvault.feature.group.repoapi.mappers.GroupMapper
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
    private val documentMapper: DocumentMapper,
    private val groupsDao: GroupsDao,
    private val groupMapper: GroupMapper,
    private val documentFileMapper: DocumentFileMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DocumentRepository {

    override suspend fun getDocumentById(id: Long): Document =
        documentMapper.toDocument(documentsDao.getFullDocument(id))

    override suspend fun deleteDocumentFile(documentId: Long, fileName: String) {
        documentsDao.deleteDocumentFileByIdAndName(documentId, fileName)
    }

    override suspend fun deleteDocumentById(documentId: Long) {
        documentsDao.deleteDocumentAndFilesById(documentId)
    }

    override suspend fun addDraftDocument(): DraftDocument = withContext(ioDispatcher) {
        val defaultGroup = groupsDao.getFirstGroup()
        val nowDate = dateTimeUtils.getDateTimeUTCNow()
        val folderDate = dateTimeUtils.formatToDocumentFolder(nowDate)
        val id = documentsDao.insert(
            DocumentEntity(
                name = "",
                documentFolderName = "",
                createdDate = nowDate,
                groupId = defaultGroup.groupEntity.groupId
            )
        )
        val folderName = "${id}_$folderDate"
        documentsDao.updateProductFolderName(folderName, id)
        DraftDocument(
            id = id,
            date = nowDate,
            documentFolderName = folderName,
            group = groupMapper.toGroup(defaultGroup)
        )
    }

    override fun getAllDocumentsFlow(): Flow<List<Document>> = documentsDao.getFullDocumentsFlow()
        .map { list -> documentMapper.toDocumentList(list) }

    override suspend fun addDocument(createDocument: CreateDocument) {
        val entity = documentMapper.toDocumentEntity(createDocument)
        val list = documentFileMapper.toFileDataEntity(createDocument)
        documentsDao.updateDocumentAndFiles(
            documentEntity = entity,
            list = list
        )
    }

    override suspend fun addDocument(document: Document) {
        documentsDao.insert(documentMapper.toDocumentEntity(document))
    }

    override suspend fun addDocuments(documents: List<Document>) = withContext(ioDispatcher) {
        documents.map { document ->
            async {
                documentsDao.insertDocumentAndFiles(
                    documentEntity = documentMapper.toDocumentEntity(document),
                    list = documentFileMapper.toDocumentFileEntityList(
                        documentId = document.documentId,
                        files = document.files
                    )
                )
            }
        }.awaitAll()
        Unit
    }

    override suspend fun removeDocumentById(id: Long) {
        documentsDao.deleteDocumentById(id)
    }

    override suspend fun updateDocumentWithFiles(document: Document, files: List<DocumentFile>) {
        val entity = documentMapper.toDocumentEntity(document)
        val filesEntities = documentFileMapper.toDocumentFileEntityList(
            documentId = document.documentId,
            files = files
        )
        documentsDao.updateDocumentAndFiles(entity, filesEntities)
    }

    override suspend fun updateFilesInDocument(documentId: Long, files: List<DocumentFile>) {
        val filesEntities = documentFileMapper.toDocumentFileEntityList(documentId, files)
        documentsDao.upsertFiles(filesEntities)
    }
}
