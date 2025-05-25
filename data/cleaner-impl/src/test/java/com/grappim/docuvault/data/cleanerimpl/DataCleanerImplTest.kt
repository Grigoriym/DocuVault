package com.grappim.docuvault.data.cleanerimpl

import com.grappim.docuvault.data.cleanerapi.DataCleaner
import com.grappim.docuvault.data.dbapi.DatabaseWrapper
import com.grappim.docuvault.feature.docgroup.repoapi.GroupRepository
import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.testing.getDocumentFileData
import com.grappim.docuvault.testing.getRandomLong
import com.grappim.docuvault.testing.getRandomString
import com.grappim.docuvault.utils.filesapi.deletion.FileDeletionUtils
import com.grappim.docuvault.utils.filesapi.pathmanager.FolderPathManager
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
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class DataCleanerImplTest {

    private val fileDeletionUtils = mockk<FileDeletionUtils>()
    private val documentRepository = mockk<DocumentRepository>()
    private val folderPathManager = mockk<FolderPathManager>()
    private val databaseWrapper = mockk<DatabaseWrapper>()
    private val groupRepository = mockk<GroupRepository>()

    private val sut: DataCleaner = DataCleanerImpl(
        dispatcher = UnconfinedTestDispatcher(),
        fileDeletionUtils = fileDeletionUtils,
        documentRepository = documentRepository,
        folderPathManager = folderPathManager,
        databaseWrapper = databaseWrapper,
        groupRepository = groupRepository
    )

    @Test
    fun `on deleteGroup should delete group`() = runTest {
        val groupId = getRandomLong()
        coEvery { groupRepository.deleteGroupById(groupId) } just Runs
        sut.deleteGroup(groupId)
        coVerify { groupRepository.deleteGroupById(groupId) }
    }

    @Test
    fun `on deleteDocumentFile, if file was deleted, return true`() = runTest {
        val docId = getRandomLong()
        val fileName = getRandomString()
        val uriString = getRandomString()

        coEvery { fileDeletionUtils.deleteFile(uriString) } returns true
        coEvery { documentRepository.deleteDocumentFile(docId, fileName) } just Runs

        val actual = sut.deleteDocumentFile(docId, fileName, uriString)

        assertTrue(actual)

        coVerify { fileDeletionUtils.deleteFile(uriString) }
        coVerify { documentRepository.deleteDocumentFile(docId, fileName) }
    }

    @Test
    fun `on deleteDocumentFile, if file was not deleted, return false`() = runTest {
        val docId = getRandomLong()
        val fileName = getRandomString()
        val uriString = getRandomString()

        coEvery { fileDeletionUtils.deleteFile(uriString) } returns false

        val actual = sut.deleteDocumentFile(docId, fileName, uriString)

        assertFalse(actual)

        coVerify { fileDeletionUtils.deleteFile(uriString) }
        coVerify(exactly = 0) { documentRepository.deleteDocumentFile(docId, fileName) }
    }

    @Test
    fun `on deleteDocumentFiles should call deleteDocumentFile for each item`() = runTest {
        val docId = getRandomLong()

        coEvery { fileDeletionUtils.deleteFile(uriString = any()) } returns true
        coEvery { documentRepository.deleteDocumentFile(docId, any()) } just Runs

        val list = listOf(
            getDocumentFileData(),
            getDocumentFileData(),
            getDocumentFileData()
        )

        sut.deleteDocumentFiles(
            documentId = docId,
            list = list
        )

        list.forEach {
            coVerify { fileDeletionUtils.deleteFile(uriString = it.uriString) }
            coVerify {
                documentRepository.deleteDocumentFile(
                    documentId = docId,
                    fileName = it.name
                )
            }
        }
    }

    @Test
    fun `on deleteDocumentData should delete folder and from repo`() = runTest {
        val docId = getRandomLong()
        val folderName = getRandomString()

        coEvery { fileDeletionUtils.deleteFolder(folderName) } just Runs
        coEvery { documentRepository.deleteDocumentById(docId) } just Runs

        sut.deleteDocumentData(docId, folderName)

        coVerify { fileDeletionUtils.deleteFolder(folderName) }
        coVerify { documentRepository.deleteDocumentById(docId) }
    }

    @Test
    fun `on deleteTempFolder should get correct folder name and remove it`() = runTest {
        val folderName = getRandomString()

        coEvery { fileDeletionUtils.deleteFolder(any()) } just Runs
        every { folderPathManager.getTempFolderName(any()) } returns "${folderName}_temp"

        sut.deleteTempFolder(folderName)

        verify { folderPathManager.getTempFolderName(folderName) }
        coVerify { fileDeletionUtils.deleteFolder("${folderName}_temp") }
    }

    @Test
    fun `on deleteBackupFolder should get correct folder name and remove it`() = runTest {
        val folderName = getRandomString()

        coEvery { fileDeletionUtils.deleteFolder(any()) } just Runs
        every { folderPathManager.getBackupFolderName(any()) } returns "${folderName}_backup"

        sut.deleteBackupFolder(folderName)

        verify { folderPathManager.getBackupFolderName(folderName) }
        coVerify { fileDeletionUtils.deleteFolder("${folderName}_backup") }
    }

    @Test
    fun `on clearAllData, should call the needed functions`() = runTest {
        coEvery { databaseWrapper.clearAllTables() } just Runs
        coEvery { fileDeletionUtils.clearMainFolder() } returns true

        sut.clearAllData()

        coVerify { databaseWrapper.clearAllTables() }
        coVerify { fileDeletionUtils.clearMainFolder() }
    }
}
