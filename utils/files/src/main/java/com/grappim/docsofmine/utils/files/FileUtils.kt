package com.grappim.docsofmine.utils.files

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.grappim.docsofmine.utils.datetime.DateTimeUtils
import com.grappim.docsofmine.utils.files.mime.MimeTypes
import com.grappim.domain.model.document.Document
import com.grappim.domain.model.document.DocumentFileData
import com.grappim.domain.model.document.DraftDocument
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

    fun getDocumentFolderName(
        id: String,
        gDriveFormattedDate: String
    ): String = "${id}_${gDriveFormattedDate}"

    fun removeFile(fileData: FileData): Boolean {
        val file = File(fileData.uri.path)
        (fileData.preview as? Uri)?.path?.let { File(it).delete() }
        return file.delete()
    }

    fun deleteFolder(document: DraftDocument) {
        val file = getFolder(document.folderName)
        file.deleteRecursively()
    }

    fun formatFileSize(
        fileSize: Long
    ): String =
        android.text.format.Formatter.formatShortFileSize(context, fileSize)

    private fun createFileLocally(
        uri: Uri,
        folderName: String
    ): File {
        val localFile = File(getFolder(folderName), getUriFileName(uri))
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

    fun getFileUrisFromGalleryUri(
        uri: Uri,
        draftDocument: DraftDocument
    ): FileData {
        Timber.d("getFileUrisFromGalleryUri, $uri")
        val newFile = createFileLocally(uri, draftDocument.folderName)
        val newUri = getFileUri(newFile)
        val fileSize = getUriFileSize(newUri)
        val mimeType = getMimeType(uri)
        return FileData(
            uri = newUri,
            name = getUriFileName(newUri),
            size = fileSize,
            sizeToDemonstrate = formatFileSize(fileSize),
            mimeType = mimeType,
            preview = newUri,
            mimeTypeToDemonstrate = MimeTypes.formatMimeType(mimeType)
        )
    }

    fun getFileDataFromCameraPicture(
        uri: Uri
    ): FileData {
        Timber.d("getFileUrisFromUri, $uri")
        val fileSize = getUriFileSize(uri)
        val mimeType = getMimeType(uri)
        return FileData(
            uri = uri,
            preview = uri,
            name = getUriFileName(uri),
            size = fileSize,
            sizeToDemonstrate = formatFileSize(fileSize),
            mimeType = mimeType,
            mimeTypeToDemonstrate = MimeTypes.formatMimeType(mimeType)
        )
    }

    fun getFileUrisFromUri(
        uri: Uri,
        draftDocument: DraftDocument
    ): FileData {
        Timber.d("getFileUrisFromUri, $uri")
        val newFile = createFileLocally(uri, draftDocument.folderName)
        val newUri = getFileUri(newFile)
        val fileSize = getUriFileSize(newUri)
        val mimeType = getMimeType(uri)
        return FileData(
            uri = newUri,
            preview = getFilePreview(
                uri = newUri,
                folderName = draftDocument.folderName,
                mimeType = mimeType
            ),
            name = getUriFileName(newUri),
            size = fileSize,
            sizeToDemonstrate = formatFileSize(fileSize),
            mimeType = mimeType,
            mimeTypeToDemonstrate = MimeTypes.formatMimeType(mimeType)
        )
    }

    fun getFilePreview(
        uri: Uri,
        folderName: String,
        mimeType: String
    ): Uri? {
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

    fun getMimeType(uri: Uri): String =
        context.contentResolver.getType(uri)
            ?: MimeTypeMap.getSingleton().getMimeTypeFromExtension(File(uri.path).extension)
            ?: error("Cannot get mimeType from $uri")

    @Throws(IllegalStateException::class)
    fun createFileFromDocumentFileUri(
        document: Document,
        documentFileData: DocumentFileData
    ): File {
        val uri = Uri.parse(documentFileData.uriString)
        val tempFile = File(
            getFolder(
                document.getGDriveFileName(
                    dateTimeUtils.formatToGDrive(document.createdDate)
                )
            ),
            documentFileData.name
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

    fun getFileUriForTakePicture(
        folderName: String
    ): Uri = getFileUri(
        folderName,
        getBitmapFileName()
    )

    fun getFileUri(
        folderName: String,
        fileName: String
    ): Uri {
        val tmpFile = File(getFolder(folderName), fileName)
        return getFileUri(tmpFile)
    }

    fun getFileUri(
        file: File
    ): Uri {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        Timber.d("getTmpFileUri, $uri")
        return uri
    }

    fun fromBitmapToUri(
        bitmap: Bitmap,
        folderName: String,
    ): Uri {
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

    private fun getUriFileSize(
        uri: Uri
    ): Long {
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
            val file = File(uri.path)
            file.length()
        }
    }

    private fun getUriFileName(
        uri: Uri
    ): String {
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
            val file = File(uri.path)
            file.name
        }
    }
}