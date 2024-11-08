package com.grappim.docuvault.utils.files.mappers

import com.grappim.docuvault.feature.docs.domain.DocumentFile
import com.grappim.docuvault.feature.docs.uiapi.DocumentFileUI

interface FileDataMapper {
    suspend fun toDocumentFileData(documentFileUI: DocumentFileUI): DocumentFile
    suspend fun toDocumentFileDataList(list: List<DocumentFileUI>): List<DocumentFile>

    suspend fun toDocumentFileUiData(documentFile: DocumentFile): DocumentFileUI
    suspend fun toDocumentFileUiDataList(list: List<DocumentFile>): List<DocumentFileUI>
}
