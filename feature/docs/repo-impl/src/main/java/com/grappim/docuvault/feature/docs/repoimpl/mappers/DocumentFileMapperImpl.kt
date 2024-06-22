package com.grappim.docuvault.feature.docs.repoimpl.mappers

import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.feature.docs.db.model.DocumentFileEntity
import com.grappim.docuvault.feature.docs.domain.CreateDocument
import com.grappim.docuvault.feature.docs.domain.DocumentFile
import com.grappim.docuvault.feature.docs.repoapi.mappers.DocumentFileMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DocumentFileMapperImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DocumentFileMapper {
    override suspend fun toFileDataEntity(
        createDocument: CreateDocument
    ): List<DocumentFileEntity> = withContext(ioDispatcher) {
        createDocument.files.map { file ->
            DocumentFileEntity(
                fileId = file.fileId,
                documentId = createDocument.id,
                name = file.name,
                mimeType = file.mimeType,
                size = file.size,
                uriString = file.uriString,
                md5 = file.md5
            )
        }
    }

    override suspend fun toDocumentFileEntityList(
        documentId: Long,
        files: List<DocumentFile>
    ): List<DocumentFileEntity> = withContext(ioDispatcher) {
        files.map { file -> toDocumentFileEntity(documentId, file) }
    }

    override suspend fun toDocumentFileEntity(
        documentId: Long,
        documentFile: DocumentFile
    ): DocumentFileEntity = withContext(ioDispatcher) {
        DocumentFileEntity(
            fileId = documentFile.fileId,
            documentId = documentId,
            name = documentFile.name,
            mimeType = documentFile.mimeType,
            size = documentFile.size,
            uriString = documentFile.uriString,
            md5 = documentFile.md5
        )
    }
}
