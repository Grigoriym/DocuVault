package com.grappim.docuvault.utils.files.inforetriever

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.utils.datetimeapi.DateTimeUtils
import com.grappim.docuvault.utils.files.MimeTypesHandler
import com.grappim.docuvault.utils.filesapi.inforetriever.FileInfoRetriever
import com.grappim.docuvault.utils.filesapi.pathmanager.FolderPathManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileInfoRetrieverImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mimeTypesHandler: MimeTypesHandler,
    private val dateTimeUtils: DateTimeUtils,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val folderPathManager: FolderPathManager
) : FileInfoRetriever {

    override suspend fun findFileInFolder(fileName: String, folderName: String): File =
        withContext(ioDispatcher) {
            val folder = folderPathManager.getMainFolder(folderName)
            val foundFile = folder.walk().filter { it.name.startsWith(fileName) }.firstOrNull()
                ?: error("file not found")
            foundFile
        }

    override fun getFileExtension(uri: Uri): String {
        val mimeType = getMimeType(uri)
        return mimeTypesHandler.formatMimeType(mimeType)
    }

    override fun getMimeType(uri: Uri): String {
        val uriPath = requireNotNull(uri.path)
        return context.contentResolver.getType(uri)
            ?: MimeTypeMap.getSingleton().getMimeTypeFromExtension(File(uriPath).extension)
            ?: error("Cannot get mimeType from $uri")
    }

    override fun getFileSize(uri: Uri): Long {
        val returnCursor = context.contentResolver.query(
            uri,
            arrayOf(OpenableColumns.SIZE),
            null,
            null,
            null
        )
        return if (returnCursor != null) {
            val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
            var size: Long
            returnCursor.use {
                returnCursor.moveToFirst()
                size = returnCursor.getString(sizeIndex).toLong()
            }
            size
        } else {
            val uriPath = requireNotNull(uri.path)
            val file = File(uriPath)
            file.length()
        }
    }

    override fun getFileName(uri: Uri): String {
        val returnCursor = context.contentResolver.query(
            uri,
            arrayOf(OpenableColumns.DISPLAY_NAME),
            null,
            null,
            null
        )
        return if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            var name: String
            returnCursor.use {
                returnCursor.moveToFirst()
                name = returnCursor.getString(nameIndex)
            }
            name
        } else {
            val uriPath = requireNotNull(uri.path)
            val file = File(uriPath)
            file.name
        }
    }

    /**
     * 2024-01-23_21-04-46_1706040286629.jpg
     */
    override fun getFileName(extension: String): String {
        val date = dateTimeUtils.formatToDocumentFolder(OffsetDateTime.now())
        val millis = dateTimeUtils.getInstantNow().toEpochMilli()
        return "${date}_$millis.$extension"
    }

    override fun getBitmapFileName(): String = getFileName("jpg")
}
