package com.grappim.docuvault.utils.files.docfilemanager

import com.grappim.docuvault.utils.files.pathmanager.FolderPathManager
import com.grappim.docuvault.utils.files.transfer.FileTransferOperations
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DocumentFileManagerImpl @Inject constructor(
    private val folderPathManager: FolderPathManager,
    private val fileTransferOperations: FileTransferOperations
) : DocumentFileManager {
    /**
     * Copy the initial state of images so that we can revert them in case they are deleted from
     * the original folder
     */
    override suspend fun copyToBackupFolder(documentFolderName: String) {
        fileTransferOperations.copySourceFilesToDestination(
            sourceFolderName = documentFolderName,
            destinationFolderName = folderPathManager.getBackupFolderName(documentFolderName)
        )
    }

    override suspend fun moveFromTempToOriginalFolder(documentFolderName: String) {
        fileTransferOperations.moveSourceFilesToDestinationFolder(
            sourceFolderName = folderPathManager.getTempFolderName(documentFolderName),
            destinationFolderName = documentFolderName
        )
    }

    override suspend fun moveFromBackupToOriginalFolder(documentFolderName: String) {
        fileTransferOperations.moveSourceFilesToDestinationFolder(
            sourceFolderName = folderPathManager.getBackupFolderName(documentFolderName),
            destinationFolderName = documentFolderName
        )
    }
}
