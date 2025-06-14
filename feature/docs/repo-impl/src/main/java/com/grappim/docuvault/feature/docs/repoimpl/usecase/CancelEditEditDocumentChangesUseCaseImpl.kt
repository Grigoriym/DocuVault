package com.grappim.docuvault.feature.docs.repoimpl.usecase

import com.grappim.docuvault.data.backupapi.BackupFilesRepository
import com.grappim.docuvault.data.cleanerapi.DataCleaner
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.feature.docs.repoapi.usecase.CancelDocumentChangesData
import com.grappim.docuvault.feature.docs.repoapi.usecase.CancelEditDocumentChangesUseCase
import com.grappim.docuvault.utils.filesapi.docfilemanager.DocumentFileManager
import javax.inject.Inject

class CancelEditEditDocumentChangesUseCaseImpl @Inject constructor(
    private val documentRepository: DocumentRepository,
    private val dataCleaner: DataCleaner,
    private val backupFilesRepository: BackupFilesRepository,
    private val documentFileManager: DocumentFileManager
) : CancelEditDocumentChangesUseCase {

    override suspend fun cancel(data: CancelDocumentChangesData) {
        val initialFiles = backupFilesRepository.getAllByDocumentId(data.documentId)
        documentRepository.updateFilesInDocument(data.documentId, initialFiles)
        documentFileManager.moveFromBackupToOriginalFolder(data.documentFolderName)

        dataCleaner.deleteTempFolder(data.documentFolderName)
        dataCleaner.deleteBackupFolder(data.documentFolderName)

        backupFilesRepository.deleteFilesByDocumentId(data.documentId)
    }
}
