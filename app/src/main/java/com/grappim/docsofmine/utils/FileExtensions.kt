package com.grappim.docsofmine.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.util.*

@Throws(IllegalStateException::class)
fun createFileFromDocumentFileUri(
    context: Context,
    uri: Uri
): File {
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw IllegalStateException("can not create inputStream from $uri")
    val tempFile = File.createTempFile("image", UUID.randomUUID().toString())
    inputStream.use { input ->
        val outputStream = FileOutputStream(tempFile)
        outputStream.use { output ->
            val buffer = ByteArray(4 * 1024)
            while (true) {
                val byteCount = input.read(buffer)
                if (byteCount < 0) break
                output.write(buffer, 0, byteCount)
            }
            output.flush()
        }
    }
    return tempFile
}

fun Uri.getFileNameAndSize(
    context: Context
): Pair<String, Int> {
    val returnCursor = context.contentResolver.query(
        this,
        null,
        null,
        null,
        null
    )
    if (returnCursor != null) {
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = returnCursor.getString(sizeIndex).toInt()
        returnCursor.close()
        return Pair(name, size)
    } else {
        throw IllegalStateException("returnCursor is null for $this")
    }
}