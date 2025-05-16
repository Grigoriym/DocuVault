package com.grappim.docuvault.utils.filesapi.urimanager

import android.net.Uri
import com.grappim.docuvault.feature.docs.uiapi.DocumentFileUI
import com.grappim.docuvault.utils.filesapi.models.CameraTakePictureData

interface FileUriManager {

    suspend fun getDocumentFileDataList(
        uriList: List<Uri>,
        folderName: String,
        isEdit: Boolean = false
    ): List<DocumentFileUI>

    suspend fun getDocumentFileData(
        uri: Uri,
        folderName: String,
        isEdit: Boolean = false
    ): DocumentFileUI

    fun getFileDataFromCameraPicture(
        cameraTakePictureData: CameraTakePictureData,
        isEdit: Boolean
    ): DocumentFileUI

    fun getFileUriForTakePicture(folderName: String, isEdit: Boolean = false): CameraTakePictureData
}
