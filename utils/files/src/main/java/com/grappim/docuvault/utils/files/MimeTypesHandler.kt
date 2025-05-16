package com.grappim.docuvault.utils.files

import com.grappim.docuvault.utils.filesapi.Application
import com.grappim.docuvault.utils.filesapi.Image
import com.grappim.docuvault.utils.filesapi.Text
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MimeTypesHandler @Inject constructor() {

    fun formatMimeType(mimeType: String): String = when (mimeType) {
        Image.PNG -> "png"
        Image.JPEG -> "jpg"

        Application.PPT -> "ppt"
        Application.PPTX -> "pptx"
        Application.DOC -> "doc"
        Application.DOCX -> "docx"
        Application.XSL -> "xsl"
        Application.XSLX -> "xslx"

        Application.PDF -> "pdf"
        Application.JSON -> "json"

        Text.PLAIN -> "txt"
        Text.RTF -> "rtf"

        else -> "unknown"
    }
}
