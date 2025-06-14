package com.grappim.docuvault.feature.docs.repoapi.usecase

/**
 * This use case is needed to do all the necessary stuff that is required when
 * we want to edit the document
 * 1. Copy files from original to temp folder
 * 2. Insert files to backup folder
 */
interface EditDocumentPreparationUseCase {
    suspend fun prepare(documentId: Long): PreparedEditDocumentData
}
