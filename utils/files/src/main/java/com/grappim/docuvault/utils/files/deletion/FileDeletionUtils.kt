package com.grappim.docuvault.utils.files.deletion

import android.net.Uri

interface FileDeletionUtils {
    suspend fun deleteFolder(folderName: String)
    suspend fun deleteFile(uriString: String): Boolean
    suspend fun deleteFile(uri: Uri): Boolean
    suspend fun clearMainFolder(): Boolean
}
