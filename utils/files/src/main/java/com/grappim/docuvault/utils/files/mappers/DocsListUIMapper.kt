package com.grappim.docuvault.utils.files.mappers

import com.grappim.docuvault.feature.docs.domain.Document
import com.grappim.docuvault.utils.files.models.DocumentListUI

interface DocsListUIMapper {
    suspend fun toDocumentListUIList(list: List<Document>): List<DocumentListUI>
}
