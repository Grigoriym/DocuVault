package com.grappim.docuvault.feature.docs.repoimpl.usecase

import com.grappim.docuvault.data.backupapi.BackupFilesRepository
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.feature.docs.repoapi.usecase.EditDocumentPreparationUseCase
import com.grappim.docuvault.feature.docs.repoapi.usecase.PreparedEditDocumentData
import com.grappim.docuvault.utils.filesapi.docfilemanager.DocumentFileManager
import javax.inject.Inject

class EditDocumentPreparationUseCaseImpl @Inject constructor(
    private val documentFileManager: DocumentFileManager,
    private val documentRepository: DocumentRepository,
    private val backupFilesRepository: BackupFilesRepository
) : EditDocumentPreparationUseCase {

    override suspend fun prepare(documentId: Long): PreparedEditDocumentData {
        val document = documentRepository.getDocumentById(documentId)
        documentFileManager.copyToBackupFolder(document.documentFolderName)
        backupFilesRepository.insertFiles(documentId, document.files)
        return PreparedEditDocumentData(document)
    }
}
