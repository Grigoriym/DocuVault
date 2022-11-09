package com.grappim.docsofmine.data.mappers

import com.grappim.docsofmine.data.db.model.document.DocumentEntity
import com.grappim.docsofmine.data.db.model.document.DocumentFileDataEntity
import com.grappim.docsofmine.data.db.model.group.GroupEntity
import com.grappim.docsofmine.data.db.model.group.GroupFieldEntity
import com.grappim.docsofmine.data.model.document.DocumentDTO
import com.grappim.docsofmine.data.model.document.DocumentFileUriDTO
import com.grappim.docsofmine.data.model.group.GroupDTO
import com.grappim.docsofmine.data.model.group.GroupFieldDTO
import com.grappim.domain.model.document.CreateDocument
import com.grappim.domain.model.document.Document
import com.grappim.domain.model.document.DocumentFileData
import com.grappim.domain.model.group.Group
import com.grappim.domain.model.group.GroupField

fun Document.toDTO(): DocumentDTO =
    DocumentDTO(
        id = this.id,
        name = this.name,
        group = GroupDTO(
            id = this.group.id,
            name = this.group.name,
            fields = this.group.fields.map { field ->
                GroupFieldDTO(
                    name = field.name,
                    value = field.value,
                )
            },
            color = this.group.color
        ),
        filesUri = this.filesUri.map { uri ->
            DocumentFileUriDTO(
                name = uri.name,
                mimeType = uri.mimeType,
                path = uri.uriPath,
                string = uri.uriString,
                size = uri.size,
                previewUriString = uri.previewUriString,
                previewUriPath = uri.previewUriPath
            )
        },
        createdDate = this.createdDate
    )

fun DocumentDTO.toDomain(): Document =
    Document(
        id = this.id,
        name = this.name,
        group = Group(
            id = this.group.id,
            name = this.group.name,
            fields = this.group.fields.map { field ->
                GroupField(
                    name = field.name,
                    value = field.value,
                )
            },
            color = this.group.color
        ),
        filesUri = this.filesUri.map { uri ->
            DocumentFileData(
                name = uri.name,
                mimeType = uri.mimeType,
                uriPath = uri.path,
                uriString = uri.string,
                size = uri.size,
                previewUriString = uri.previewUriString,
                previewUriPath = uri.previewUriPath
            )
        },
        createdDate = this.createdDate
    )

fun DocumentEntity.toDocument(
    fileDataList: List<DocumentFileDataEntity>?
): Document =
    Document(
        id = this.documentId,
        name = this.name,
        group = Group(
            id = this.documentId,
            name = this.group?.name ?: "",
            fields = this.group?.fields?.map { field ->
                GroupField(
                    name = field.name,
                    value = field.value
                )
            } ?: emptyList(),
            color = this.group?.color ?: ""
        ),
        filesUri = fileDataList?.map { dto ->
            DocumentFileData(
                name = dto.name,
                mimeType = dto.mimeType,
                uriPath = dto.uriPath,
                uriString = dto.uriString,
                size = dto.size,
                previewUriPath = dto.previewUriPath,
                previewUriString = dto.previewUriString
            )
        } ?: emptyList(),
        createdDate = this.createdDate
    )

fun CreateDocument.toFileDataEntityList(): List<DocumentFileDataEntity> =
    filesUri.map {
        DocumentFileDataEntity(
            documentId = this.id,
            name = it.name,
            mimeType = it.mimeType,
            size = it.size,
            uriPath = it.uriPath,
            uriString = it.uriString,
            previewUriString = it.previewUriString,
            previewUriPath = it.previewUriPath
        )
    }

fun Document.toFileDataEntityList(): List<DocumentFileDataEntity> =
    filesUri.map {
        DocumentFileDataEntity(
            documentId = this.id,
            name = it.name,
            mimeType = it.mimeType,
            size = it.size,
            uriPath = it.uriPath,
            uriString = it.uriString,
            previewUriString = it.previewUriString,
            previewUriPath = it.previewUriPath
        )
    }

fun CreateDocument.toEntity(): DocumentEntity =
    DocumentEntity(
        documentId = this.id,
        name = this.name,
        group = GroupEntity(
            name = this.group.name,
            fields = this.group.fields.map { field ->
                GroupFieldEntity(
                    name = field.name,
                    value = field.value,
                )
            },
            color = this.group.color
        ),
//        filesUri = this.filesUri.map { uri ->
//            DocumentFileUriDTO(
//                name = uri.name,
//                mimeType = uri.mimeType,
//                path = uri.uriPath,
//                string = uri.uriString,
//                size = uri.size,
//                previewUriString = uri.previewUriString,
//                previewUriPath = uri.previewUriPath
//            )
//        },
        createdDate = this.createdDate
    )

fun Document.toEntity(): DocumentEntity =
    DocumentEntity(
        documentId = this.id,
        name = this.name,
        group = GroupEntity(
            name = this.group.name,
            fields = this.group.fields.map { field ->
                GroupFieldEntity(
                    name = field.name,
                    value = field.value,
                )
            },
            color = this.group.color
        ),
//        filesUri = this.filesUri.map { uri ->
//            DocumentFileUriDTO(
//                name = uri.name,
//                mimeType = uri.mimeType,
//                path = uri.uriPath,
//                string = uri.uriString,
//                size = uri.size,
//                previewUriPath = uri.previewUriPath,
//                previewUriString = uri.previewUriString
//            )
//        },
        createdDate = this.createdDate
    )