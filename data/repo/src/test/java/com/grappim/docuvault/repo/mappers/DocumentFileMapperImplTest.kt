package com.grappim.docuvault.repo.mappers

import com.grappim.docuvault.data.db.model.document.DocumentFileEntity
import com.grappim.docuvault.repo.getCreateDocument
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class DocumentFileMapperImplTest {

    private val sut = DocumentFileMapperImpl(UnconfinedTestDispatcher())

    @Test
    fun `toFileDataEntity should return correct list of files`() = runTest {
        val createDocument = getCreateDocument()
        val actual = sut.toFileDataEntity(createDocument)
        val first = createDocument.files.first()
        val second = createDocument.files[1]
        val expected = listOf(
            DocumentFileEntity(
                fileId = first.fileId,
                documentId = createDocument.id,
                name = first.name,
                mimeType = first.mimeType,
                size = first.size,
                uriString = first.uriString,
                md5 = first.md5
            ),
            DocumentFileEntity(
                fileId = second.fileId,
                documentId = createDocument.id,
                name = second.name,
                mimeType = second.mimeType,
                size = second.size,
                uriString = second.uriString,
                md5 = second.md5
            )
        )
        assertEquals(expected, actual)
    }
}
