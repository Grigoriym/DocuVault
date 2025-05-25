package com.grappim.docuvault.utils.files

import org.junit.Assert.assertEquals
import org.junit.Test

class MimeTypesHandlerTest {

    private val sut = MimeTypesHandler()

    @Test
    fun `on correct formatMimeType should return correct strings`() {
        assertEquals("png", sut.formatMimeType("image/png"))
        assertEquals("jpg", sut.formatMimeType("image/jpeg"))
        assertEquals("ppt", sut.formatMimeType("application/vnd.ms-powerpoint"))
        assertEquals(
            "pptx",
            sut.formatMimeType(
                "application/vnd.openxmlformats-officedocument.presentationml.presentation"
            )
        )
        assertEquals("doc", sut.formatMimeType("application/msword"))
        assertEquals(
            "docx",
            sut.formatMimeType(
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            )
        )
        assertEquals("pdf", sut.formatMimeType("application/pdf"))
        assertEquals("json", sut.formatMimeType("application/json"))
        assertEquals("txt", sut.formatMimeType("text/plain"))
        assertEquals("rtf", sut.formatMimeType("text/rtf"))
    }

    @Test
    fun `on incorrect formatMimeType should return unknown`() {
        assertEquals("unknown", sut.formatMimeType("g23ggdsgd"))
        assertEquals("unknown", sut.formatMimeType("application/t198y"))
    }
}
