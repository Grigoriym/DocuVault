package com.grappim.docuvault.utils.files.mappers

import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.feature.docs.repoapi.models.Document
import com.grappim.docuvault.feature.docs.uiapi.DocumentListUI
import com.grappim.docuvault.uikit.utils.ColorUtils
import com.grappim.docuvault.utils.datetimeapi.DateTimeUtils
import com.grappim.docuvault.utils.filesapi.mappers.DocsListUIMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DocsListUIMapperImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val dateTimeUtils: DateTimeUtils,
    private val colorUtils: ColorUtils
) : DocsListUIMapper {
    override suspend fun toDocumentListUIList(list: List<Document>): List<DocumentListUI> =
        withContext(ioDispatcher) {
            list.map { document: Document ->
                val formattedDate = dateTimeUtils.formatToDemonstrate(document.createdDate)
                DocumentListUI(
                    id = document.documentId.toString(),
                    name = document.name,
                    createdDate = formattedDate,
                    groupColor = colorUtils.toComposeColor(document.group.color),
                    preview = document.files.map { it.uriString }
                )
            }
        }
}
