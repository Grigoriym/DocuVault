package com.grappim.docuvault.feature.docs.repoimpl

import app.cash.turbine.test
import com.grappim.docuvault.data.dbapi.DatabaseWrapper
import com.grappim.docuvault.feature.docgroup.db.model.GroupEntity
import com.grappim.docuvault.feature.docgroup.db.model.GroupWithFieldsEntity
import com.grappim.docuvault.feature.docgroup.repoapi.mappers.GroupMapper
import com.grappim.docuvault.feature.docgroup.repoapi.model.Group
import com.grappim.docuvault.feature.docs.db.model.DocumentEntity
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.feature.docs.repoapi.mappers.DocumentFileMapper
import com.grappim.docuvault.feature.docs.repoapi.mappers.DocumentMapper
import com.grappim.docuvault.feature.docs.repoapi.models.DraftDocument
import com.grappim.docuvault.testing.getCreateDocument
import com.grappim.docuvault.testing.getDocument
import com.grappim.docuvault.testing.getDocumentEntity
import com.grappim.docuvault.testing.getDocumentFile
import com.grappim.docuvault.testing.getDocumentFileEntity
import com.grappim.docuvault.testing.getFullDocumentEntity
import com.grappim.docuvault.testing.getRandomLong
import com.grappim.docuvault.testing.getRandomString
import com.grappim.docuvault.utils.datetimeapi.DateTimeUtils
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.OffsetDateTime
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class DocumentRepositoryImplTest {
    private lateinit var sut: DocumentRepository

    private val dateTimeUtils = mockk<DateTimeUtils>()
    private val documentMapper = mockk<DocumentMapper>()
    private val databaseWrapper = mockk<DatabaseWrapper>()
    private val groupMapper = mockk<GroupMapper>()
    private val documentFileMapper = mockk<DocumentFileMapper>()

    private val defaultGroup = GroupWithFieldsEntity(
        groupEntity = GroupEntity(
            groupId = 5381,
            name = "Ricky Fischer",
            color = "porttitor"
        ),
        groupFields = emptyList()
    )
    private val nowDate = OffsetDateTime.now()
    private val folderDate = "asdasd"
    private val group = Group(id = 1809, name = "Dena Vincent", fields = emptyList(), color = "ridens")

    @Before
    fun setup() {
        sut = DocumentRepositoryImpl(
            dateTimeUtils = dateTimeUtils,
            documentMapper = documentMapper,
            databaseWrapper = databaseWrapper,
            groupMapper = groupMapper,
            documentFileMapper = documentFileMapper,
            ioDispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `on getDocumentById returns document`() = runTest {
        val documentId = getRandomLong()
        val fullDocumentEntity = getFullDocumentEntity()
        val expected = getDocument()

        coEvery { databaseWrapper.documentsDao.getFullDocument(documentId) } returns fullDocumentEntity
        coEvery { documentMapper.toDocument(fullDocumentEntity) } returns expected

        val actual = sut.getDocumentById(documentId)

        coVerify { databaseWrapper.documentsDao.getFullDocument(documentId) }
        coVerify { documentMapper.toDocument(fullDocumentEntity) }

        assertEquals(expected, actual)
    }

    @Test
    fun `on deleteDocumentFile deletes the document`() = runTest {
        val documentId = getRandomLong()
        val fileName = getRandomString()
        coEvery { databaseWrapper.documentsDao.deleteDocumentFileByIdAndName(documentId, fileName) } just Runs

        sut.deleteDocumentFile(documentId, fileName)

        coVerify { databaseWrapper.documentsDao.deleteDocumentFileByIdAndName(documentId, fileName) }
    }

    @Test
    fun `on deleteDocumentById deletes the document`() = runTest {
        val documentId = getRandomLong()
        coEvery { databaseWrapper.documentsDao.deleteDocumentAndFilesById(documentId) } just Runs

        sut.deleteDocumentById(documentId)

        coVerify { databaseWrapper.documentsDao.deleteDocumentAndFilesById(documentId) }
    }

    @Test
    fun `on addDraftDocument returns draftDocument`() = runTest {
        every { databaseWrapper.groupsDao.getFirstGroup() } returns defaultGroup
        every { dateTimeUtils.getDateTimeUTCNow() } returns nowDate
        every { dateTimeUtils.formatToDocumentFolder(nowDate) } returns folderDate
        val docToInsert = DocumentEntity(
            name = "",
            createdDate = nowDate,
            documentFolderName = "",
            groupId = defaultGroup.groupEntity.groupId
        )
        val draftDocumentId = 654L
        coEvery { databaseWrapper.documentsDao.insert(docToInsert) } returns draftDocumentId
        val folderName = "${draftDocumentId}_$folderDate"
        coEvery { databaseWrapper.documentsDao.updateProductFolderName(folderName, draftDocumentId) } just Runs
        coEvery { groupMapper.toGroup(defaultGroup) } returns group
        val expected = DraftDocument(
            id = draftDocumentId,
            date = nowDate,
            documentFolderName = folderName,
            group = group
        )

        val actual = sut.addDraftDocument()

        verify { databaseWrapper.groupsDao.getFirstGroup() }
        verify { dateTimeUtils.getDateTimeUTCNow() }
        verify { dateTimeUtils.formatToDocumentFolder(nowDate) }
        coVerify { databaseWrapper.documentsDao.insert(docToInsert) }
        coVerify { databaseWrapper.documentsDao.updateProductFolderName(folderName, draftDocumentId) }
        coVerify { groupMapper.toGroup(defaultGroup) }

        assertEquals(actual, expected)
    }

    @Test
    fun `on getAllDocumentsFlow returns documents`() = runTest {
        val entitiesList = listOf(getFullDocumentEntity())
        val expected = listOf(getDocument())
        coEvery { databaseWrapper.documentsDao.getFullDocumentsFlow() } returns flowOf(entitiesList)
        coEvery { documentMapper.toDocumentList(entitiesList) } returns expected

        sut.getAllDocumentsFlow().test {
            assertEquals(expected, awaitItem())

            coVerify { documentMapper.toDocumentList(entitiesList) }

            awaitComplete()
        }
    }

    @Test
    fun `on addDocument with createDocument adds the document`() = runTest {
        val createDocument = getCreateDocument()
        val entity = getDocumentEntity()
        val filesEntities = listOf(getDocumentFileEntity())
        coEvery { documentMapper.toDocumentEntity(createDocument) } returns entity
        coEvery { documentFileMapper.toFileDataEntity(createDocument) } returns filesEntities
        coEvery { databaseWrapper.documentsDao.updateDocumentAndFiles(entity, filesEntities) } just Runs

        sut.addDocument(createDocument)

        coVerify { documentMapper.toDocumentEntity(createDocument) }
        coVerify { documentFileMapper.toFileDataEntity(createDocument) }
        coVerify { databaseWrapper.documentsDao.updateDocumentAndFiles(entity, filesEntities) }
    }

    @Test
    fun `on addDocument with document adds the document`() = runTest {
        val document = getDocument()
        val entity = getDocumentEntity()
        coEvery { documentMapper.toDocumentEntity(document) } returns entity
        coEvery { databaseWrapper.documentsDao.insert(entity) } returns 1L

        sut.addDocument(document)

        coVerify { documentMapper.toDocumentEntity(document) }
        coVerify { databaseWrapper.documentsDao.insert(entity) }
    }

    @Test
    fun `on addDocuments with documents adds the documents`() = runTest {
        val documents = listOf(getDocument())
        val entitiesList = listOf(getDocumentEntity())
        val filesEntities = listOf(getDocumentFileEntity())

        coEvery { documentMapper.toDocumentEntity(documents[0]) } returns entitiesList[0]
        coEvery { documentFileMapper.toDocumentFileEntityList(documents[0].documentId, documents[0].files) } returns filesEntities
        coEvery { databaseWrapper.documentsDao.insertDocumentAndFiles(entitiesList[0], filesEntities) } just Runs

        sut.addDocuments(documents)

        coVerify { documentMapper.toDocumentEntity(documents[0]) }
        coVerify { documentFileMapper.toDocumentFileEntityList(documents[0].documentId, documents[0].files) }
        coVerify { databaseWrapper.documentsDao.insertDocumentAndFiles(entitiesList[0], filesEntities) }
    }

    @Test
    fun `on removeDocumentById removes the document`() = runTest {
        val documentId = getRandomLong()
        coEvery { databaseWrapper.documentsDao.deleteDocumentById(documentId) } just Runs

        sut.removeDocumentById(documentId)

        coVerify { databaseWrapper.documentsDao.deleteDocumentById(documentId) }
    }

    @Test
    fun `on updateDocumentWithFiles updates the document`() = runTest {
        val document = getDocument()
        val entity = getDocumentEntity()
        val filesEntities = listOf(getDocumentFileEntity())
        val newFiles = listOf(getDocumentFile())
        coEvery { documentMapper.toDocumentEntity(document) } returns entity
        coEvery { documentFileMapper.toDocumentFileEntityList(document.documentId, newFiles) } returns filesEntities
        coEvery { databaseWrapper.documentsDao.updateDocumentAndFiles(entity, filesEntities) } just Runs

        sut.updateDocumentWithFiles(document, newFiles)

        coVerify { documentMapper.toDocumentEntity(document) }
        coVerify { documentFileMapper.toDocumentFileEntityList(document.documentId, newFiles) }
        coVerify { databaseWrapper.documentsDao.updateDocumentAndFiles(entity, filesEntities) }
    }

    @Test
    fun `on updateFilesInDocument updates the document`() = runTest {
        val documentId = getRandomLong()
        val files = listOf(getDocumentFile())
        val filesEntities = listOf(getDocumentFileEntity())
        coEvery { documentFileMapper.toDocumentFileEntityList(documentId, files) } returns filesEntities
        coEvery { databaseWrapper.documentsDao.upsertFiles(filesEntities) } just Runs

        sut.updateFilesInDocument(documentId, files)

        coVerify { documentFileMapper.toDocumentFileEntityList(documentId, files) }
        coVerify { databaseWrapper.documentsDao.upsertFiles(filesEntities) }
    }

    @Test
    fun `on getDocumentsByGroupId returns documents`() = runTest {
        val groupId = getRandomLong()
        val entitiesList = listOf(getFullDocumentEntity())
        val expected = listOf(getDocument())
        coEvery { databaseWrapper.documentsDao.getDocumentsByGroupId(groupId) } returns entitiesList
        coEvery { documentMapper.toDocumentList(entitiesList) } returns expected

        val actual = sut.getDocumentsByGroupId(groupId)

        coVerify { databaseWrapper.documentsDao.getDocumentsByGroupId(groupId) }
        coVerify { documentMapper.toDocumentList(entitiesList) }
        assertEquals(expected, actual)
    }
}
