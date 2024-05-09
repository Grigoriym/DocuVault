package com.grappim.docuvault.utils.files.mappers

import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.datetime.DateTimeUtils
import com.grappim.docuvault.uikit.utils.toColor
import com.grappim.docuvault.utils.files.models.DocumentListUI
import com.grappim.domain.model.document.Document
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DocsListUIMapperImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val dateTimeUtils: DateTimeUtils
) : DocsListUIMapper {
    override suspend fun toDocumentListUIList(list: List<Document>): List<DocumentListUI> =
        withContext(ioDispatcher) {
            list.map { document: Document ->
                val formattedDate = dateTimeUtils.formatToDemonstrate(document.createdDate)
                DocumentListUI(
                    id = document.documentId.toString(),
                    name = document.name,
                    createdDate = formattedDate,
                    groupColor = document.group.color.toColor(),
                    preview = document.filesUri.firstOrNull()?.uriString ?: ""
                )
            }
        }
}
