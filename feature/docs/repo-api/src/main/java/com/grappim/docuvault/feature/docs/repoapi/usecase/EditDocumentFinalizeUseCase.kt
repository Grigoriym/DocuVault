package com.grappim.docuvault.feature.docs.repoapi.usecase

/**
 * This use case is needed to do all the necessary stuff that is required when
 * we want to save the edited document
 * 1. Move files from temp to original folder
 * 2. Delete temp folder
 * 3. Delete backup folder
 * 4. Delete backup files
 * 5. Update document in database
 */
interface EditDocumentFinalizeUseCase {
    suspend fun finalize(data: EditDocumentFinalizeData)
}
