package com.grappim.docuvault.utils.files.transfer

interface FileTransferOperations {
    suspend fun moveSourceFilesToDestinationFolder(
        sourceFolderName: String,
        destinationFolderName: String
    )

    suspend fun copySourceFilesToDestination(
        sourceFolderName: String,
        destinationFolderName: String
    )
}
