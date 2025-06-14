package com.grappim.docuvault.feature.docs.repoimpl.mappers

import com.grappim.docuvault.feature.docgroup.repoapi.model.Group
import com.grappim.docuvault.feature.docs.db.model.DocumentEntity
import com.grappim.docuvault.feature.docs.repoapi.models.Document
import com.grappim.docuvault.testing.getCreateDocument
import com.grappim.docuvault.testing.getDocument
import com.grappim.docuvault.testing.getFullDocumentEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class DocumentMapperImplTest {

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

    @Test
    fun `toDocumentEntity with Document should return correct documentEntity`() = runTest {
        val document = getDocument()
        val actual = sut.toDocumentEntity(document)
        val expected = DocumentEntity(
            documentId = document.documentId,
            name = document.name,
            createdDate = document.createdDate,
            documentFolderName = document.documentFolderName,
            isCreated = true,
            groupId = document.group.id
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `toDocument should return correct document`() = runTest {
        val fullDocumentEntity = getFullDocumentEntity()
        val actual = sut.toDocument(fullDocumentEntity)
        val expected = Document(
            documentId = fullDocumentEntity.documentEntity.documentId,
            name = fullDocumentEntity.documentEntity.name,
            group = Group(
                id = fullDocumentEntity.group.groupId,
                name = fullDocumentEntity.group.name,
                fields = emptyList(),
                color = fullDocumentEntity.group.color
            ),
            files = emptyList(),
            createdDate = fullDocumentEntity.documentEntity.createdDate,
            documentFolderName = fullDocumentEntity.documentEntity.documentFolderName
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `toDocumentList should return correct list of documents`() = runTest {
        val list = listOf(getFullDocumentEntity(), getFullDocumentEntity())
        val actual = sut.toDocumentList(list)
        val expected = listOf(
            Document(
                documentId = list[0].documentEntity.documentId,
                name = list[0].documentEntity.name,
                group = Group(
                    id = list[0].group.groupId,
                    name = list[0].group.name,
                    fields = emptyList(),
                    color = list[0].group.color
                ),
                files = emptyList(),
                createdDate = list[0].documentEntity.createdDate,
                documentFolderName = list[0].documentEntity.documentFolderName
            ),
            Document(
                documentId = list[1].documentEntity.documentId,
                name = list[1].documentEntity.name,
                group = Group(
                    id = list[1].group.groupId,
                    name = list[1].group.name,
                    fields = emptyList(),
                    color = list[1].group.color
                ),
                files = emptyList(),
                createdDate = list[1].documentEntity.createdDate,
                documentFolderName = list[1].documentEntity.documentFolderName
            )
        )
        assertEquals(expected, actual)
    }
}
