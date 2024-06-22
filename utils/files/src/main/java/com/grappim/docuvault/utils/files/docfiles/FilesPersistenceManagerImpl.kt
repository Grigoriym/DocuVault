package com.grappim.docuvault.utils.files.docfiles

import com.grappim.docuvault.feature.docs.domain.DocumentFile
import com.grappim.docuvault.feature.docs.uiapi.DocumentFileUI
import com.grappim.docuvault.utils.files.mappers.FileDataMapper
import javax.inject.Inject

class FilesPersistenceManagerImpl @Inject constructor(
    private val docFilesMapper: FileDataMapper
) : FilesPersistenceManager {

    /**
     * Prepares a list of edited images for persistence by updating their URI paths.
     *
     * This function iterates through a list of [DocumentFileUI] objects and checks for images marked as edited.
     * For each edited image, it removes the "_temp" suffix from its URI path and URI string,
     * indicating that the image is ready to be moved from a temporary editing location to a permanent storage location.
     * Images not marked as edited are left unchanged.
     *
     * @param files A list of [DocumentFileUI] objects to be processed.
     * @return A list of [DocumentFile] objects with updated URI paths for edited images.
     */
    override suspend fun prepareEditedFilesToPersist(
        files: List<DocumentFileUI>
    ): List<DocumentFile> = files.map { image ->
        if (image.isEdit) {
            val result = docFilesMapper.toDocumentFileData(image)
            val newUriString = result.uriString.replace("_temp", "")
            result.copy(
                uriString = newUriString
            )
        } else {
            docFilesMapper.toDocumentFileData(image)
        }
    }
}
