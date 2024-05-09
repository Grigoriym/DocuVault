package com.grappim.docuvault.utils.files.urimanager

import android.net.Uri
import com.grappim.docuvault.uikit.DocumentFileUiData
import com.grappim.docuvault.utils.files.models.CameraTakePictureData

interface FileUriManager {
    fun getFileUriFromGalleryUri(
        uri: Uri,
        folderName: String,
        isEdit: Boolean = false
    ): DocumentFileUiData

    fun getFileDataFromCameraPicture(
        cameraTakePictureData: CameraTakePictureData,
        isEdit: Boolean
    ): DocumentFileUiData

    fun getFileUriForTakePicture(folderName: String, isEdit: Boolean = false): CameraTakePictureData
}
