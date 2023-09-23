package com.grappim.docsofmine.utils

import androidx.core.net.toUri
import com.grappim.docsofmine.uikit.MimeTypeImageHelper
import com.grappim.docsofmine.utils.files.FileUtils
import com.grappim.docsofmine.utils.files.mime.MimeTypes
import com.grappim.domain.model.document.Document
import javax.inject.Inject

class FilePreviewHelper @Inject constructor(
    private val mimeTypeImageHelper: MimeTypeImageHelper,
    private val fileUtils: FileUtils
) {

    fun getPreview(
        mimeType: String,
        fileDataUriString: String,
        document: Document
    ): Any? {
        val uri = fileDataUriString.toUri()

        val preview: Any? = when (mimeType) {
            in MimeTypes.images -> {
                fileDataUriString
            }
            MimeTypes.Application.PDF -> {
                fileUtils.getFilePreview(
                    uri = uri,
                    folderName = fileUtils.getDocumentFolderName(
                        document = document
                    ),
                    mimeType = mimeType
                )
            }
            else -> {
                mimeTypeImageHelper.getImageByMimeType(mimeType)
            }
        }
        return preview
    }
}