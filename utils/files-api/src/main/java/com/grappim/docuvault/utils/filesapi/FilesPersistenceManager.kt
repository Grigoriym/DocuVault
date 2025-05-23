package com.grappim.docuvault.utils.filesapi

import com.grappim.docuvault.feature.docs.domain.DocumentFile
import com.grappim.docuvault.feature.docs.uiapi.DocumentFileUI

interface FilesPersistenceManager {
    suspend fun prepareEditedFilesToPersist(files: List<DocumentFileUI>): List<DocumentFile>
}
