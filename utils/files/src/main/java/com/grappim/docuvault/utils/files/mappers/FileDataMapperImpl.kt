package com.grappim.docuvault.utils.files.mappers

import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.uikit.DocumentFileUiData
import com.grappim.domain.model.document.DocumentFileData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FileDataMapperImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FileDataMapper {
    override suspend fun toDocumentFileData(
        documentFileUiData: DocumentFileUiData
    ): DocumentFileData = withContext(ioDispatcher) {
        DocumentFileData(
            fileId = documentFileUiData.fileId,
            name = documentFileUiData.name,
            mimeType = documentFileUiData.mimeType,
            uriString = documentFileUiData.uri.toString(),
            size = documentFileUiData.size,
            md5 = documentFileUiData.md5
        )
    }

    override suspend fun toDocumentFileDataList(
        list: List<DocumentFileUiData>
    ): List<DocumentFileData> = list.map { uiData ->
        toDocumentFileData(uiData)
    }
}
