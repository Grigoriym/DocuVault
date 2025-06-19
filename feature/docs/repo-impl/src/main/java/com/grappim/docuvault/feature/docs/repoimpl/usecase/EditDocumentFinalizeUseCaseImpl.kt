package com.grappim.docuvault.feature.docs.repoimpl.usecase

import com.grappim.docuvault.data.backupapi.BackupFilesRepository
import com.grappim.docuvault.data.cleanerapi.DataCleaner
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.feature.docs.repoapi.models.Document
import com.grappim.docuvault.feature.docs.repoapi.usecase.EditDocumentFinalizeData
import com.grappim.docuvault.feature.docs.repoapi.usecase.EditDocumentFinalizeUseCase
import com.grappim.docuvault.utils.filesapi.docfilemanager.DocumentFileManager
import javax.inject.Inject

class EditDocumentFinalizeUseCaseImpl @Inject constructor(
    private val documentFileManager: DocumentFileManager,
    private val dataCleaner: DataCleaner,
    private val backupFilesRepository: BackupFilesRepository,
    private val documentRepository: DocumentRepository
) : EditDocumentFinalizeUseCase {

    override suspend fun finalize(data: EditDocumentFinalizeData) {
        val document = Document(
            documentId = data.documentId,
            name = data.documentName,
            createdDate = data.createdDate,
            group = data.group,
            files = data.editedFiles,
            documentFolderName = data.documentFolderName
        )
        documentFileManager.moveFromTempToOriginalFolder(data.documentFolderName)
        dataCleaner.deleteTempFolder(data.documentFolderName)

        dataCleaner.deleteBackupFolder(data.documentFolderName)
        backupFilesRepository.deleteFilesByDocumentId(data.documentId)

        documentRepository.updateDocumentWithFiles(document, data.editedFiles)
    }
}
