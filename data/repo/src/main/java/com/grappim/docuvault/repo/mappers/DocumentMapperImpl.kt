package com.grappim.docuvault.repo.mappers

import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.data.db.model.FullDocumentEntity
import com.grappim.docuvault.data.db.model.document.DocumentEntity
import com.grappim.docuvault.data.db.model.document.DocumentFileEntity
import com.grappim.docuvault.feature.group.domain.Group
import com.grappim.docuvault.feature.group.domain.GroupField
import com.grappim.domain.model.document.CreateDocument
import com.grappim.domain.model.document.Document
import com.grappim.domain.model.document.DocumentFileData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
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
                filesUri = fullDocumentEntity.documentFiles.map {
                    DocumentFileData(
                        name = it.name,
                        mimeType = it.mimeType,
                        uriString = it.uriString,
                        size = it.size,
                        md5 = it.md5,
                        fileId = it.fileId
                    )
                },
                createdDate = fullDocumentEntity.documentEntity.createdDate,
                documentFolderName = fullDocumentEntity.documentEntity.documentFolderName,
                isCreated = fullDocumentEntity.documentEntity.isCreated
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
}

fun DocumentEntity.toDocument(fileDataList: List<DocumentFileEntity>?): Document = Document(
    documentId = this.documentId,
    name = this.name,
    group = Group(
        id = this.documentId,
        name = "",
//        name = this.group?.name ?: "",
        fields = emptyList(),
//            fields = this.group?.fields?.map { field ->
//                GroupField(
//                    name = field.name,
//                    value = field.value
//                )
//            } ?: emptyList(),
//        color = this.group?.color ?: "",
        color = ""
    ),
    filesUri = fileDataList?.map { entity ->
        DocumentFileData(
            name = entity.name,
            mimeType = entity.mimeType,
            uriString = entity.uriString,
            size = entity.size,
            md5 = entity.md5,
            fileId = entity.fileId
        )
    } ?: emptyList(),
    createdDate = this.createdDate,
    documentFolderName = this.documentFolderName,
    isCreated = this.isCreated
)

fun Document.toFileDataEntityList(): List<DocumentFileEntity> = filesUri.map {
    DocumentFileEntity(
        documentId = this.documentId,
        name = it.name,
        mimeType = it.mimeType,
        size = it.size,
        uriString = it.uriString,
        md5 = it.md5
    )
}

fun Document.toEntity(): DocumentEntity = DocumentEntity(
    documentId = this.documentId,
    name = this.name,
    createdDate = OffsetDateTime.now(),
    documentFolderName = "",
    groupId = 0
)
