package com.grappim.docsofmine.utils.files

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.grappim.docsofmine.utils.datetime.DateTimeUtils
import com.grappim.domain.Document
import com.grappim.domain.DocumentFileUri
import com.grappim.domain.DraftDocument
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileUtils @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dateTimeUtils: DateTimeUtils
) {

    fun removeFile(fileUris: FileUris): Boolean {
        val file = File(fileUris.fileUri.path)
        fileUris.filePreviewUri?.path?.let { File(it).delete() }
        return file.delete()
    }

    fun deleteFolder(document: DraftDocument) {
        val file = getFolder(document.folderName)
        file.deleteRecursively()
    }

    fun formatFileSize(
        fileSize: Int
    ): String =
        android.text.format.Formatter.formatShortFileSize(context, fileSize.toLong())

    fun getFileUrisFromGalleryUri(
        uri: Uri,
        draftDocument: DraftDocument
    ): FileUris {
        Timber.d("getFileUrisFromGalleryUri, $uri")
        val newFile = createFileLocally(uri, draftDocument)
        val newUri = Uri.fromFile(newFile)
        val nameAndSize = getFileNameAndSize(newUri)
        return FileUris(
            fileUri = newUri,
            fileName = nameAndSize.first,
            fileSize = formatFileSize(nameAndSize.second),
            mimeType = getMimeType(newUri)
        )
    }

    private fun createFileLocally(
        uri: Uri,
        draftDocument: DraftDocument
    ): File {
        val nameAndSize = getFileNameAndSize(uri)
        val localFile = File(getFolder(draftDocument.folderName), nameAndSize.first)
        writeDataToFile(uri, localFile)
        Timber.d("createFileLocally, $localFile")
        return localFile
    }

    private fun writeDataToFile(uri: Uri, newFile: File) {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: error("can not create inputStream from $uri")

        inputStream.use { input ->
            val outputStream = FileOutputStream(newFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024)
                Timber.d("writeDataToFile: $output")
                while (true) {
                    val byteCount = input.read(buffer)
                    Timber.d("writeDataToFile: byteCount $byteCount")
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
    }

    fun getFileUrisFromUriPicture(
        uri: Uri,
        draftDocument: DraftDocument
    ): FileUris {
        Timber.d("getFileUrisFromUri, $uri")
        val nameAndSize = getFileNameAndSize(uri)
        return FileUris(
            fileUri = uri,
            filePreviewUri = getFilePreview(uri, draftDocument),
            fileName = nameAndSize.first,
            fileSize = formatFileSize(nameAndSize.second),
            mimeType = getMimeType(uri)
        )
    }

    fun getFileUrisFromUri(
        uri: Uri,
        draftDocument: DraftDocument
    ): FileUris {
        Timber.d("getFileUrisFromUri, $uri")
        val newFile = createFileLocally(uri, draftDocument)
        val newUri = Uri.fromFile(newFile)
        val nameAndSize = getFileNameAndSize(newUri)
        return FileUris(
            fileUri = newUri,
            filePreviewUri = getFilePreview(newUri, draftDocument),
            fileName = nameAndSize.first,
            fileSize = formatFileSize(nameAndSize.second),
            mimeType = getMimeType(newUri)
        )
    }

    private fun getFilePreview(uri: Uri, draftDocument: DraftDocument): Uri? {
        val mimetype = getMimeType(uri)
        Timber.d("$uri's mimeType: $mimetype")
        if (mimetype == PDF_MIME_TYPE) {
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
            return fromBitmapToUri(bitmap!!, draftDocument)
        } else {
            return null
        }
    }

    fun getMimeType(uri: Uri): String =
        context.contentResolver.getType(uri)
            ?: MimeTypeMap.getSingleton().getMimeTypeFromExtension(File(uri.path).extension)
            ?: error("Cannot get mimeType from $uri")

    @Throws(IllegalStateException::class)
    fun createFileFromDocumentFileUri(
        document: Document,
        documentFileUri: DocumentFileUri
    ): File {
        val uri = Uri.parse(documentFileUri.string)
        val tempFile = File(
            getFolder(
                document.getGDriveFileName(
                    dateTimeUtils.formatToGDrive(document.createdDate)
                )
            ),
            documentFileUri.fileName
        )
        if (tempFile.exists()) {
            return tempFile
        }
        writeDataToFile(uri, tempFile)
        return tempFile
    }

    private fun getFileName(): String {
        val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
        return dtf.format(LocalDateTime.now())
    }

    private fun getBitmapFileName(
        prefix: String = ""
    ): String =
        StringBuilder()
            .apply {
                if (prefix.isNotEmpty()) {
                    append("${prefix}_")
                }
                append(getFileName())
                append("_")
                append("${Instant.now().toEpochMilli()}")
                append(".jpg")
            }
            .toString()

    fun getFolder(
        child: String
    ): File {
        val folder = File(context.filesDir, "docs/$child")
        if (folder.exists().not()) {
            folder.mkdirs()
        }
        return folder
    }

    fun getTmpFileUri(
        draftDocument: DraftDocument
    ): Uri {
        val tmpFile = File(getFolder(draftDocument.folderName), getBitmapFileName())
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            tmpFile
        )
    }

    fun fromBitmapToUri(
        bitmap: Bitmap,
        draftDocument: DraftDocument
    ): Uri {
        val tempFile = File(getFolder(draftDocument.folderName), getBitmapFileName("preview"))
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes)
        val bitmapData = bytes.toByteArray()

        val fileOutPut = FileOutputStream(tempFile)
        fileOutPut.write(bitmapData)
        fileOutPut.flush()
        fileOutPut.close()
        val uri = Uri.fromFile(tempFile)
        return uri
    }

    fun getFileNameAndSize(
        uri: Uri
    ): Pair<String, Int> {
        Timber.d("$uri to getFileNameAndSize")
        val returnCursor = context.contentResolver.query(
            uri,
            arrayOf(OpenableColumns.SIZE, OpenableColumns.DISPLAY_NAME),
            null,
            null,
            null
        )
        return if (returnCursor != null) {
            val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            val name = returnCursor.getString(nameIndex)
            val size = returnCursor.getString(sizeIndex).toInt()
            returnCursor.close()
            Pair(name, size)
        } else {
            val file = File(uri.path)
            val name = file.name
            val size = file.length()
            Pair(name, size.toInt())
        }
    }
}