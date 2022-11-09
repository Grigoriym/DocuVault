package com.grappim.docsofmine.data

import com.grappim.docsofmine.common.async.ApplicationCoroutineScope
import com.grappim.docsofmine.utils.files.FileUtils
import com.grappim.domain.model.document.DraftDocument
import com.grappim.domain.repository.DocumentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DataCleaner @Inject constructor(
    private val fileUtils: FileUtils,
    private val documentRepository: DocumentRepository,
    @ApplicationCoroutineScope private val appDispatcher: CoroutineScope
) {

    suspend fun clearDocumentData(
        draftDocument: DraftDocument
    ) = appDispatcher.launch() {
        Timber.d("start cleaning")
        fileUtils.deleteFolder(draftDocument)
        documentRepository.removeDocumentById(draftDocument.id)
    }.join()
}