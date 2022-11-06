package com.grappim.docsofmine.data.mappers

import com.grappim.docsofmine.data.db.model.DocumentEntity
import com.grappim.docsofmine.data.db.model.DocumentFileUriDTO
import com.grappim.docsofmine.data.db.model.GroupEntity
import com.grappim.docsofmine.data.db.model.GroupFieldEntity
import com.grappim.docsofmine.data.model.DocumentDTO
import com.grappim.docsofmine.data.model.GroupDTO
import com.grappim.docsofmine.data.model.GroupFieldDTO
import com.grappim.domain.CreateDocument
import com.grappim.domain.Document
import com.grappim.domain.DocumentFileUri
import com.grappim.domain.Group
import com.grappim.domain.GroupField

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
                fileName = uri.fileName,
                mimeType = uri.mimeType,
                path = uri.path,
                string = uri.string,
                fileSize = uri.size
            )
        },
        createdDate = this.createdDate
    )

fun DocumentEntity.toDocument(
    createdDateString: String
): Document =
    Document(
        id = this.id,
        name = this.name,
        group = Group(
            id = this.id,
            name = this.group?.name ?: "",
            fields = this.group?.fields?.map { field ->
                GroupField(
                    name = field.name,
                    value = field.value
                )
            } ?: emptyList(),
            color = this.group?.color ?: ""
        ),
        filesUri = this.filesUri.map { dto ->
            DocumentFileUri(
                fileName = dto.fileName,
                mimeType = dto.mimeType,
                path = dto.path,
                string = dto.string,
                size = dto.fileSize
            )
        },
        createdDate = this.createdDate,
        createdDateString = createdDateString
    )

fun CreateDocument.toEntity(): DocumentEntity =
    DocumentEntity(
        id = this.id,
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
        filesUri = this.filesUri.map { uri ->
            DocumentFileUriDTO(
                fileName = uri.fileName,
                mimeType = uri.mimeType,
                path = uri.path,
                string = uri.string,
                fileSize = uri.size
            )
        },
        createdDate = this.createdDate
    )