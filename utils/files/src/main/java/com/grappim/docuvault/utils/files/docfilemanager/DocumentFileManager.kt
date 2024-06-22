package com.grappim.docuvault.utils.files.docfilemanager

interface DocumentFileManager {
    suspend fun copyToBackupFolder(documentFolderName: String)

    suspend fun moveFromTempToOriginalFolder(documentFolderName: String)

    suspend fun moveFromBackupToOriginalFolder(documentFolderName: String)
}
