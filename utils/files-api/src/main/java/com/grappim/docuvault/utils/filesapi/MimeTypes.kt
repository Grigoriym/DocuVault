package com.grappim.docuvault.utils.filesapi

val mimeTypesForDocumentPicker = arrayOf(
    Application.PDF,

    Application.DOC,
    Application.DOCX,
    Application.XSL,
    Application.XSLX,
    Application.PPT,
    Application.PPTX,

    Application.ODS,
    Application.ODP,
    Application.ODT,

    Text.RTF,
    Text.PLAIN
)

val images = listOf(
    Image.PNG,
    Image.JPEG
)

object Image {
    const val PREFIX = "image/"

    const val JPEG = PREFIX + "jpeg"
    const val PNG = PREFIX + "png"
}

object Text {
    const val PREFIX = "text/"

    const val PLAIN = PREFIX + "plain"
    const val RTF = PREFIX + "rtf"
}

object Application {
    const val PREFIX = "application/"

    const val JSON = PREFIX + "json"

    const val PDF = PREFIX + "pdf"

    const val DOC = PREFIX + "msword"
    const val DOCX = PREFIX + "vnd.openxmlformats-officedocument.wordprocessingml.document"
    const val PPT = PREFIX + "vnd.ms-powerpoint"
    const val PPTX = PREFIX + "vnd.openxmlformats-officedocument.presentationml.presentation"
    const val XSL = PREFIX + "vnd.ms-excel"
    const val XSLX = PREFIX + "vnd.openxmlformats-officedocument.spreadsheetml.sheet"

    const val ODT = PREFIX + "vnd.oasis.opendocument.text"
    const val ODS = PREFIX + "vnd.oasis.opendocument.spreadsheet"
    const val ODP = PREFIX + "vnd.oasis.opendocument.presentation"
}
