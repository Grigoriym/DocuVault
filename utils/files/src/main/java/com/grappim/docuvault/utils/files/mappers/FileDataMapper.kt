package com.grappim.docuvault.utils.files.mappers

import com.grappim.docuvault.uikit.DocumentFileUiData
import com.grappim.domain.model.document.DocumentFileData

interface FileDataMapper {
    suspend fun toDocumentFileData(documentFileUiData: DocumentFileUiData): DocumentFileData
    suspend fun toDocumentFileDataList(list: List<DocumentFileUiData>): List<DocumentFileData>
}
