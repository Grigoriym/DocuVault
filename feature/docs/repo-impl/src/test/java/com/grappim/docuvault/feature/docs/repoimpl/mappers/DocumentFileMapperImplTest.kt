package com.grappim.docuvault.feature.docs.repoimpl.mappers

import com.grappim.docuvault.feature.docs.db.model.DocumentFileEntity
import com.grappim.docuvault.testing.getCreateDocument
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
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
