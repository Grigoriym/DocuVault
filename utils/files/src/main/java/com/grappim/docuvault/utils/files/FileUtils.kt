package com.grappim.docuvault.utils.files

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.grappim.docuvault.datetime.DateTimeUtils
import com.grappim.docuvault.feature.docs.domain.Document
import com.grappim.docuvault.feature.docs.domain.DocumentFile
import com.grappim.docuvault.feature.docs.domain.DraftDocument
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.Instant
import java.time.OffsetDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileUtils @Inject constructor(
    @ApplicationContext private val context: Context,
    private val hashUtils: HashUtils,
    private val dateTimeUtils: DateTimeUtils
) {
    fun getDocumentFolderName(document: Document): String =
        "${document.documentId}_${dateTimeUtils.formatToGDrive(document.createdDate)}"

    fun deleteFolder(document: DraftDocument) {
        val file = getFolder(document.documentFolderName)
        file.deleteRecursively()
    }

    fun formatFileSize(fileSize: Long): String =
        android.text.format.Formatter.formatShortFileSize(context, fileSize)

    private fun createFileLocally(uri: Uri, folderName: String): File {
        val extension = getUriFileExtension(uri)
        val localFile = File(getFolder(folderName), getFileName(extension))
        writeDataToFile(uri, localFile)
        Timber.d("createFileLocally, $localFile")
        return localFile
    }

    private fun getUriFileExtension(uri: Uri): String {
        val mimeType = getMimeType(uri)
        return MimeTypes.formatMimeType(mimeType)
    }

    @Suppress("NestedBlockDepth")
    private fun writeDataToFile(uri: Uri, newFile: File) {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: error("can not create inputStream from $uri")

        inputStream.use { input ->
            val outputStream = FileOutputStream(newFile)
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
    }

    fun getFileUrisFromUri(uri: Uri, draftDocument: DraftDocument): FileData {
        Timber.d("getFileUrisFromUri, $uri")
        val newFile = createFileLocally(uri, draftDocument.documentFolderName)
        val newUri = getFileUri(newFile)
        val fileSize = getUriFileSize(newUri)
        val mimeType = getMimeType(uri)
        return FileData(
            uri = newUri,
            preview = getFilePreview(
                uri = newUri,
                folderName = draftDocument.documentFolderName,
                mimeType = mimeType
            ),
            name = newFile.name,
            size = fileSize,
            sizeToDemonstrate = formatFileSize(fileSize),
            mimeType = mimeType,
            mimeTypeToDemonstrate = MimeTypes.formatMimeType(mimeType),
            md5 = hashUtils.md5(newFile)
        )
    }

    fun getFilePreview(uri: Uri, folderName: String, mimeType: String): Uri? {
        if (mimeType == MimeTypes.Application.PDF) {
            var bitmap: Bitmap? = null
            context.contentResolver.openFileDescriptor(uri, "r")?.use { parcelFileDescriptor ->
                val pdfRenderer = PdfRenderer(parcelFileDescriptor).openPage(0)
                bitmap = Bitmap.createBitmap(
                    pdfRenderer.width,
                    pdfRenderer.height,
                    Bitmap.Config.ARGB_8888
                )
                pdfRenderer.render(
                    bitmap!!,
                    null,
                    null,
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                )
                pdfRenderer.close()
            }
            return fromBitmapToUri(bitmap!!, folderName)
        } else {
            return null
        }
    }

    private fun getMimeType(uri: Uri): String = context.contentResolver.getType(uri)
        ?: MimeTypeMap.getSingleton().getMimeTypeFromExtension(File(uri.path!!).extension)
        ?: error("Cannot get mimeType from $uri")

    @Throws(IllegalStateException::class)
    fun createFileFromDocumentFileUri(document: Document, documentFile: DocumentFile): File {
        val uri = documentFile.uriString.toUri()
        val tempFile =
            File(
                getFolder(getDocumentFolderName(document)),
                documentFile.name
            )
        if (tempFile.exists()) {
            return tempFile
        }
        writeDataToFile(uri, tempFile)
        return tempFile
    }

    private fun getFileName(extension: String): String {
        val date = dateTimeUtils.formatToGDrive(OffsetDateTime.now())
        val millis = Instant.now().toEpochMilli()
        return "${date}_$millis.$extension"
    }

    private fun getBitmapFileName(prefix: String = ""): String = StringBuilder()
        .apply {
            if (prefix.isNotEmpty()) {
                append("${prefix}_")
            }
            append(getFileName("jpg"))
        }
        .toString()

    fun getFolder(child: String): File {
        val folder = File(context.filesDir, "docs/$child")
        if (folder.exists().not()) {
            folder.mkdirs()
        }
        return folder
    }

    fun getFileUri(file: File): Uri {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        Timber.d("getFileUri from FileProvider: $uri")
        return uri
    }

    @Suppress("MagicNumber")
    fun fromBitmapToUri(bitmap: Bitmap, folderName: String): Uri {
        val fileName = getBitmapFileName("preview")
        val tempFile = File(getFolder(folderName), fileName)
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes)
        val bitmapData = bytes.toByteArray()

        val fileOutPut = FileOutputStream(tempFile)
        fileOutPut.write(bitmapData)
        fileOutPut.flush()
        fileOutPut.close()
        val uri = getFileUri(tempFile)
        return uri
    }

    private fun getUriFileSize(uri: Uri): Long {
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
            val file = File(uri.path!!)
            file.length()
        }
    }
}
