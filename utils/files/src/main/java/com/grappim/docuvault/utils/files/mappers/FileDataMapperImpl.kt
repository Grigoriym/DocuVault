package com.grappim.docuvault.utils.files.mappers

import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.feature.docs.domain.DocumentFile
import com.grappim.docuvault.feature.docs.uiapi.DocumentFileUI
import com.grappim.docuvault.utils.files.UriParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FileDataMapperImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val uriParser: UriParser
) : FileDataMapper {
    override suspend fun toDocumentFileData(documentFileUI: DocumentFileUI): DocumentFile =
        withContext(ioDispatcher) {
            DocumentFile(
                fileId = documentFileUI.fileId,
                name = documentFileUI.name,
                mimeType = documentFileUI.mimeType,
                uriString = documentFileUI.uri.toString(),
                size = documentFileUI.size,
                md5 = documentFileUI.md5
            )
        }

    override suspend fun toDocumentFileDataList(list: List<DocumentFileUI>): List<DocumentFile> =
        list.map { uiData ->
            toDocumentFileData(uiData)
        }

    override suspend fun toDocumentFileUiData(documentFile: DocumentFile): DocumentFileUI =
        withContext(ioDispatcher) {
            DocumentFileUI(
                fileId = documentFile.fileId,
                uri = uriParser.parse(documentFile.uriString),
                name = documentFile.name,
                size = documentFile.size,
                mimeType = documentFile.mimeType,
                md5 = documentFile.md5,
                isEdit = documentFile.isEdit

            )
        }

    override suspend fun toDocumentFileUiDataList(list: List<DocumentFile>): List<DocumentFileUI> =
        withContext(ioDispatcher) {
            list.map {
                toDocumentFileUiData(it)
            }
        }
}
