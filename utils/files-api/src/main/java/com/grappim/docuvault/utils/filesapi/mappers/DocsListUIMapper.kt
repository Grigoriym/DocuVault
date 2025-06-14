package com.grappim.docuvault.utils.filesapi.mappers

import com.grappim.docuvault.feature.docs.repoapi.models.Document
import com.grappim.docuvault.feature.docs.uiapi.DocumentListUI

interface DocsListUIMapper {
    suspend fun toDocumentListUIList(list: List<Document>): List<DocumentListUI>
}
