package com.grappim.docuvault.feature.docs.repoimpl.mappers

import com.grappim.docuvault.feature.docs.repoimpl.getCreateDocument
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
        val expected = com.grappim.docuvault.feature.docs.db.model.DocumentEntity(
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
