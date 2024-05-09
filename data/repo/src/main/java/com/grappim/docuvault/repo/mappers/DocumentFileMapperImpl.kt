package com.grappim.docuvault.repo.mappers

import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.data.db.model.document.DocumentFileEntity
import com.grappim.domain.model.document.CreateDocument
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
}
