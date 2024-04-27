package com.grappim.docuvault.repo.mappers

import com.grappim.docuvault.data.db.model.document.DocumentEntity
import com.grappim.docuvault.data.db.model.document.DocumentFileDataEntity
import com.grappim.docuvault.data.db.model.group.GroupEntity
import com.grappim.domain.model.document.CreateDocument
import com.grappim.domain.model.document.Document
import com.grappim.domain.model.document.DocumentFileData
import com.grappim.domain.model.group.Group

fun DocumentEntity.toDocument(fileDataList: List<DocumentFileDataEntity>?): Document = Document(
    id = this.documentId,
    name = this.name,
    group = Group(
        id = this.documentId,
        name = this.group?.name ?: "",
        fields = emptyList(),
//            fields = this.group?.fields?.map { field ->
//                GroupField(
//                    name = field.name,
//                    value = field.value
//                )
//            } ?: emptyList(),
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
            previewUriString = dto.previewUriString,
            md5 = dto.md5
        )
    } ?: emptyList(),
    createdDate = this.createdDate
)

fun CreateDocument.toFileDataEntityList(): List<DocumentFileDataEntity> = filesUri.map {
    DocumentFileDataEntity(
        documentId = this.id,
        name = it.name,
        mimeType = it.mimeType,
        size = it.size,
        uriPath = it.uriPath,
        uriString = it.uriString,
        previewUriString = it.previewUriString,
        previewUriPath = it.previewUriPath,
        md5 = it.md5
    )
}

fun Document.toFileDataEntityList(): List<DocumentFileDataEntity> = filesUri.map {
    DocumentFileDataEntity(
        documentId = this.id,
        name = it.name,
        mimeType = it.mimeType,
        size = it.size,
        uriPath = it.uriPath,
        uriString = it.uriString,
        previewUriString = it.previewUriString,
        previewUriPath = it.previewUriPath,
        md5 = it.md5
    )
}

fun CreateDocument.toEntity(): DocumentEntity = DocumentEntity(
    documentId = this.id,
    name = this.name,
    group = GroupEntity(
        name = this.group.name,
//            fields = this.group.fields.map { field ->
//                GroupFieldEntity(
//                    name = field.name,
//                    value = field.value,
//                )
//            },
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

fun Document.toEntity(): DocumentEntity = DocumentEntity(
    documentId = this.id,
    name = this.name,
    group = GroupEntity(
        name = this.group.name,
//            fields = this.group.fields.map { field ->
//                GroupFieldEntity(
//                    name = field.name,
//                    value = field.value,
//                )
//            },
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
    createdDate = this.createdDate,
    isSynced = this.isSynced
)
