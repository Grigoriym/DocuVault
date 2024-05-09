package com.grappim.docuvault.repo.mappers

import com.grappim.docuvault.data.db.model.document.DocumentEntity
import com.grappim.docuvault.repo.getCreateDocument
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class DocumentMapperImplTest {

    private val sut = DocumentMapperImpl(ioDispatcher = UnconfinedTestDispatcher())

    @Test
    fun `toDocumentEntity should return correct documentEntity`() = runTest {
        val createDocument = getCreateDocument()
        val actual = sut.toDocumentEntity(createDocument)
        val expected = DocumentEntity(
            documentId = createDocument.id,
            name = createDocument.name,
            createdDate = createDocument.createdDate,
            documentFolderName = createDocument.documentFolderName,
            isCreated = true,
            groupId = createDocument.group.id
        )
        assertEquals(expected, actual)
    }
}
