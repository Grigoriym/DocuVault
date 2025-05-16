package com.grappim.docuvault.utils.ui

import com.grappim.docuvault.utils.filesapi.Application
import com.grappim.docuvault.utils.filesapi.Text
import javax.inject.Inject

class MimeTypeImageHelper @Inject constructor() {

    fun getImageByMimeType(mimeType: String): Int = when (mimeType) {
        Application.DOC -> {
            R.drawable.ic_doc
        }

        Application.DOCX -> {
            R.drawable.ic_docx
        }

        Application.PPT -> {
            R.drawable.ic_ppt
        }

        Application.PPTX -> {
            R.drawable.ic_pptx
        }

        Text.PLAIN -> {
            R.drawable.ic_txt
        }

        else -> {
            R.drawable.ic_unknown
        }
    }
}
