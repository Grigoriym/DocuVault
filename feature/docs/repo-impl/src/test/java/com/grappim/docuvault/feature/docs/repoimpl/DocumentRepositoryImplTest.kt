package com.grappim.docuvault.feature.docs.repoimpl

import com.grappim.docuvault.feature.docs.db.dao.DocumentsDao
import com.grappim.docuvault.feature.docs.db.model.DocumentEntity
import com.grappim.docuvault.feature.docs.domain.DraftDocument
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.feature.docs.repoapi.mappers.DocumentFileMapper
import com.grappim.docuvault.feature.docs.repoapi.mappers.DocumentMapper
import com.grappim.docuvault.feature.group.db.dao.GroupsDao
import com.grappim.docuvault.feature.group.db.model.GroupEntity
import com.grappim.docuvault.feature.group.db.model.GroupWithFieldsEntity
import com.grappim.docuvault.feature.group.domain.Group
import com.grappim.docuvault.feature.group.repoapi.mappers.GroupMapper
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.OffsetDateTime
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class DocumentRepositoryImplTest {
    private lateinit var sut: DocumentRepository

    private val documentsDao: DocumentsDao = mockk()
    private val dateTimeUtils: com.grappim.docuvault.utils.datetimeapi.DateTimeUtils = mockk()
    private val documentMapper: DocumentMapper = mockk()
    private val groupsDao: GroupsDao = mockk()
    private val groupMapper: GroupMapper = mockk()
    private val documentFileMapper: DocumentFileMapper = mockk()

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
            documentsDao = documentsDao,
            dateTimeUtils = dateTimeUtils,
            documentMapper = documentMapper,
            groupsDao = groupsDao,
            groupMapper = groupMapper,
            documentFileMapper = documentFileMapper,
            ioDispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `on addDraftDocument returns draftDocument`() = runTest {
        every { groupsDao.getFirstGroup() } returns defaultGroup
        every { dateTimeUtils.getDateTimeUTCNow() } returns nowDate
        every { dateTimeUtils.formatToDocumentFolder(nowDate) } returns folderDate
        val docToInsert = DocumentEntity(
            name = "",
            createdDate = nowDate,
            documentFolderName = "",
            groupId = defaultGroup.groupEntity.groupId
        )
        val draftDocumentId = 654L
        coEvery { documentsDao.insert(docToInsert) } returns draftDocumentId
        val folderName = "${draftDocumentId}_$folderDate"
        coEvery { documentsDao.updateProductFolderName(folderName, draftDocumentId) } just Runs
        coEvery { groupMapper.toGroup(defaultGroup) } returns group
        val expected = DraftDocument(
            id = draftDocumentId,
            date = nowDate,
            documentFolderName = folderName,
            group = group
        )

        val actual = sut.addDraftDocument()

        verify { groupsDao.getFirstGroup() }
        verify { dateTimeUtils.getDateTimeUTCNow() }
        verify { dateTimeUtils.formatToDocumentFolder(nowDate) }
        coVerify { documentsDao.insert(docToInsert) }
        coVerify { documentsDao.updateProductFolderName(folderName, draftDocumentId) }
        coVerify { groupMapper.toGroup(defaultGroup) }

        assertEquals(actual, expected)
    }
}
