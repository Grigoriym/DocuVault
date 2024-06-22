package com.grappim.docuvault.utils.ui

import com.grappim.docuvault.utils.files.MimeTypes
import javax.inject.Inject

class MimeTypeImageHelper @Inject constructor() {

    fun getImageByMimeType(mimeType: String): Int = when (mimeType) {
        MimeTypes.Application.DOC -> {
            R.drawable.ic_doc
        }

        MimeTypes.Application.DOCX -> {
            R.drawable.ic_docx
        }

        MimeTypes.Application.PPT -> {
            R.drawable.ic_ppt
        }

        MimeTypes.Application.PPTX -> {
            R.drawable.ic_pptx
        }

        MimeTypes.Text.PLAIN -> {
            R.drawable.ic_txt
        }

        else -> {
            R.drawable.ic_unknown
        }
    }
}
