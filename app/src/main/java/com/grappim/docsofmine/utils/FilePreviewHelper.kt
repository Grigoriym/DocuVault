package com.grappim.docsofmine.utils

import android.net.Uri
import com.grappim.docsofmine.uikit.MimeTypeImageHelper
import com.grappim.docsofmine.utils.datetime.DateTimeUtils
import com.grappim.docsofmine.utils.files.FileUtils
import com.grappim.docsofmine.utils.files.mime.MimeTypes
import java.time.OffsetDateTime
import javax.inject.Inject

class FilePreviewHelper @Inject constructor(
    private val dateTimeUtils: DateTimeUtils,
    private val mimeTypeImageHelper: MimeTypeImageHelper,
    private val fileUtils: FileUtils
) {

    fun getPreview(
        mimeType: String,
        fileDataUriString: String,
        documentCreatedDate: OffsetDateTime,
        id: String
    ): Any? {
        val formattedDate = dateTimeUtils.formatToGDrive(documentCreatedDate)
        val uri = Uri.parse(fileDataUriString)

        val preview: Any? = when (mimeType) {
            in MimeTypes.images -> {
                fileDataUriString
            }
            MimeTypes.Application.PDF -> {
                fileUtils.getFilePreview(
                    uri = uri,
                    folderName = fileUtils.getDocumentFolderName(
                        id = id,
                        gDriveFormattedDate = formattedDate
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