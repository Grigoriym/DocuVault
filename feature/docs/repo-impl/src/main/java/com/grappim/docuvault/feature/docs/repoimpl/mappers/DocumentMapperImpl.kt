package com.grappim.docuvault.feature.docs.repoimpl.mappers

import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.feature.docs.db.model.DocumentEntity
import com.grappim.docuvault.feature.docs.db.model.FullDocumentEntity
import com.grappim.docuvault.feature.docs.domain.CreateDocument
import com.grappim.docuvault.feature.docs.domain.Document
import com.grappim.docuvault.feature.docs.domain.DocumentFile
import com.grappim.docuvault.feature.docs.repoapi.mappers.DocumentMapper
import com.grappim.docuvault.feature.group.domain.Group
import com.grappim.docuvault.feature.group.domain.GroupField
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DocumentMapperImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DocumentMapper {

    override suspend fun toDocumentList(list: List<FullDocumentEntity>): List<Document> =
        withContext(ioDispatcher) {
            list.map { entity ->
                toDocument(entity)
            }
        }

    override suspend fun toDocument(fullDocumentEntity: FullDocumentEntity): Document =
        withContext(ioDispatcher) {
            Document(
                documentId = fullDocumentEntity.documentEntity.documentId,
                name = fullDocumentEntity.documentEntity.name,
                group = Group(
                    id = fullDocumentEntity.group.groupId,
                    name = fullDocumentEntity.group.name,
                    fields = fullDocumentEntity.groupFields.map {
                        GroupField(
                            groupId = it.groupId,
                            name = it.name,
                            value = it.value
                        )
                    },
                    color = fullDocumentEntity.group.color
                ),
                files = fullDocumentEntity.documentFiles.map {
                    DocumentFile(
                        name = it.name,
                        mimeType = it.mimeType,
                        uriString = it.uriString,
                        size = it.size,
                        md5 = it.md5,
                        fileId = it.fileId
                    )
                },
                createdDate = fullDocumentEntity.documentEntity.createdDate,
                documentFolderName = fullDocumentEntity.documentEntity.documentFolderName
            )
        }

    override suspend fun toDocumentEntity(createDocument: CreateDocument): DocumentEntity =
        withContext(ioDispatcher) {
            DocumentEntity(
                documentId = createDocument.id,
                name = createDocument.name,
                createdDate = createDocument.createdDate,
                documentFolderName = createDocument.documentFolderName,
                isCreated = true,
                groupId = createDocument.group.id
            )
        }

    override suspend fun toDocumentEntity(document: Document): DocumentEntity =
        withContext(ioDispatcher) {
            DocumentEntity(
                documentId = document.documentId,
                name = document.name,
                createdDate = document.createdDate,
                documentFolderName = document.documentFolderName,
                isCreated = true,
                groupId = document.group.id
            )
        }
}
