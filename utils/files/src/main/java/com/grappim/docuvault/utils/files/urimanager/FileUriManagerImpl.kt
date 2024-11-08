package com.grappim.docuvault.utils.files.urimanager

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.grappim.docuvault.feature.docs.uiapi.DocumentFileUI
import com.grappim.docuvault.utils.files.HashUtils
import com.grappim.docuvault.utils.files.creation.FileCreationUtils
import com.grappim.docuvault.utils.files.inforetriever.FileInfoRetriever
import com.grappim.docuvault.utils.files.models.CameraTakePictureData
import com.grappim.docuvault.utils.files.pathmanager.FolderPathManager
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileUriManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val folderPathManager: FolderPathManager,
    private val hashUtils: HashUtils,
    private val fileInfoRetriever: FileInfoRetriever,
    private val fileCreationUtils: FileCreationUtils
) : FileUriManager {

    override suspend fun getDocumentFileDataList(
        uriList: List<Uri>,
        folderName: String,
        isEdit: Boolean
    ): List<DocumentFileUI> {
        val result = mutableListOf<DocumentFileUI>()
        uriList.forEach { uri ->
            val documentFileUiData = getDocumentFileData(uri, folderName, isEdit)
            result.add(documentFileUiData)
        }
        return result
    }

    override suspend fun getDocumentFileData(
        uri: Uri,
        folderName: String,
        isEdit: Boolean
    ): DocumentFileUI {
        val folder = if (isEdit) {
            folderPathManager.getTempFolderName(folderName)
        } else {
            folderName
        }

        Timber.d("getFileUrisFromGalleryUri, $uri")
        val newFile = fileCreationUtils.createFileLocally(uri, folder)
        val newUri = getFileUri(newFile)
        val fileSize = fileInfoRetriever.getFileSize(newUri)
        val mimeType = fileInfoRetriever.getMimeType(uri)
        return DocumentFileUI(
            uri = newUri,
            name = newFile.name,
            size = fileSize,
            mimeType = mimeType,
            md5 = hashUtils.md5(newFile),
            isEdit = isEdit
        )
    }

    override fun getFileDataFromCameraPicture(
        cameraTakePictureData: CameraTakePictureData,
        isEdit: Boolean
    ): DocumentFileUI {
        val uri = cameraTakePictureData.uri
        val file = cameraTakePictureData.file
        Timber.d("getFileUrisFromUri, $cameraTakePictureData")
        val fileSize = fileInfoRetriever.getFileSize(uri)
        val mimeType = fileInfoRetriever.getMimeType(uri)
        return DocumentFileUI(
            uri = uri,
            name = fileInfoRetriever.getFileName(uri),
            size = fileSize,
            mimeType = mimeType,
            md5 = hashUtils.md5(file),
            isEdit = isEdit
        )
    }

    override fun getFileUriForTakePicture(
        folderName: String,
        isEdit: Boolean
    ): CameraTakePictureData {
        val folder = if (isEdit) {
            folderPathManager.getTempFolderName(folderName)
        } else {
            folderName
        }

        val fileName = fileInfoRetriever.getBitmapFileName()
        val file = File(folderPathManager.getMainFolder(folder), fileName)
        val uri = getFileUri(file)
        return CameraTakePictureData(
            uri = uri,
            file = file
        )
    }

    private fun getFileUri(file: File): Uri {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        Timber.d("getFileUri from FileProvider: $uri")
        return uri
    }
}
