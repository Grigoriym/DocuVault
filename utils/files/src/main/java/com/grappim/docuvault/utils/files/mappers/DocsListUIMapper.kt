package com.grappim.docuvault.utils.files.mappers

import com.grappim.docuvault.utils.files.models.DocumentListUI
import com.grappim.domain.model.document.Document

interface DocsListUIMapper {
    suspend fun toDocumentListUIList(list: List<Document>): List<DocumentListUI>
}
