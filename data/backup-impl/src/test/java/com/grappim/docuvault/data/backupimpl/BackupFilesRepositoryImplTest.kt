package com.grappim.docuvault.data.backupimpl

import com.grappim.docuvault.data.backupdb.BackupDocumentFileEntity
import com.grappim.docuvault.data.backupdb.BackupFilesDao
import com.grappim.docuvault.feature.docs.domain.DocumentFile
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class BackupFilesRepositoryImplTest {

    private val backupFilesDao = mockk<BackupFilesDao>()
    private val backupFileMapper = mockk<BackupFileMapper>()

    private val sut = BackupFilesRepositoryImpl(
        backupFilesDao = backupFilesDao,
        backupFileMapper = backupFileMapper
    )

    private val documentId = 1L

    private val files = listOf(
        DocumentFile(
            fileId = 3541,
            name = "Hallie Ross",
            mimeType = "pretium",
            uriString = "dicunt",
            size = 8489,
            md5 = "molestiae",
            isEdit = false
        ),
        DocumentFile(
            fileId = 6050,
            name = "Hubert Garner",
            mimeType = "eruditi",
            uriString = "dolores",
            size = 3611,
            md5 = "auctor",
            isEdit = false
        )
    )

    private val entities = listOf(
        BackupDocumentFileEntity(
            fileId = 5694,
            documentId = 1469,
            name = "Jarrod Moran",
            mimeType = "et",
            size = 1629,
            uriString = "verear",
            md5 = "ac"
        ),
        BackupDocumentFileEntity(
            fileId = 8056,
            documentId = 2526,
            name = "Dawn Mayo",
            mimeType = "accusata",
            size = 2239,
            uriString = "antiopam",
            md5 = "debet"
        )
    )

    @Test
    fun `on insertFiles, maps the files and inserts them`() = runTest {
        coEvery {
            backupFileMapper.toBackupDocumentFileDataEntity(
                documentId,
                files
            )
        } returns entities
        coEvery { backupFilesDao.insert(entities) } just Runs

        sut.insertFiles(documentId, files)

        coVerify { backupFileMapper.toBackupDocumentFileDataEntity(documentId, files) }
        coVerify { backupFilesDao.insert(entities) }
    }

    @Test
    fun `on deleteFiles, maps the files and deletes them`() = runTest {
        coEvery {
            backupFileMapper.toBackupDocumentFileDataEntity(
                documentId,
                files
            )
        } returns entities
        coEvery { backupFilesDao.delete(entities) } just Runs

        sut.deleteFiles(documentId, files)

        coVerify { backupFileMapper.toBackupDocumentFileDataEntity(documentId, files) }
        coVerify { backupFilesDao.delete(entities) }
    }

    @Test
    fun `on deleteFilesByDocumentId, deletes files in dao`() = runTest {
        coEvery { backupFilesDao.deleteFilesByDocumentId(documentId) } just Runs

        sut.deleteFilesByDocumentId(documentId)

        coVerify { backupFilesDao.deleteFilesByDocumentId(documentId) }
    }

    @Test
    fun `on getAllByDocumentId, maps the files and returns files`() = runTest {
        coEvery { backupFilesDao.getAllFilesByDocumentId(documentId) } returns entities
        coEvery { backupFileMapper.toDocumentFileData(entities) } returns files

        sut.getAllByDocumentId(documentId)

        coVerify { backupFilesDao.getAllFilesByDocumentId(documentId) }
        coVerify { backupFileMapper.toDocumentFileData(entities) }
    }
}
