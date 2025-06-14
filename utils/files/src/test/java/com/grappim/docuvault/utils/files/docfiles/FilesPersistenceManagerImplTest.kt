package com.grappim.docuvault.utils.files.docfiles

import android.net.Uri
import com.grappim.docuvault.feature.docs.repoapi.models.DocumentFile
import com.grappim.docuvault.feature.docs.uiapi.DocumentFileUI
import com.grappim.docuvault.utils.filesapi.FilesPersistenceManager
import com.grappim.docuvault.utils.filesapi.mappers.FileDataMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE
)
class FilesPersistenceManagerImplTest {
    private val docFilesMapper: FileDataMapper = mockk()

    private lateinit var filesPersistenceManager: FilesPersistenceManager

    @Before
    fun setUp() {
        filesPersistenceManager = FilesPersistenceManagerImpl(
            docFilesMapper = docFilesMapper
        )
    }

    @Test
    fun `prepareEditedImagesToPersist should return correct list of ProductImageData`() = runTest {
        val files = listOf(
            DocumentFileUI(
                fileId = 1L,
                uri = Uri.parse("http://example.com/path_temp/image.jpg"),
                name = "image.jpg",
                size = 100L,
                mimeType = "image/jpeg",
                md5 = "abc123",
                isEdit = true
            ),
            DocumentFileUI(
                fileId = 2L,
                uri = Uri.parse("http://example.com/path/image2.jpg"),
                name = "image2.jpg",
                size = 200L,
                mimeType = "image/jpeg",
                md5 = "def456",
                isEdit = false
            )
        )

        val expectedProductImages = listOf(
            DocumentFile(
                fileId = 1L,
                name = "image.jpg",
                mimeType = "image/jpeg",
                uriString = "http://example.com/path/image.jpg",
                size = 100L,
                md5 = "abc123",
                isEdit = true
            ),
            DocumentFile(
                fileId = 2L,
                name = "image2.jpg",
                mimeType = "image/jpeg",
                uriString = "http://example.com/path/image2.jpg",
                size = 200L,
                md5 = "def456",
                isEdit = false
            )
        )

        files.forEach { file ->
            coEvery {
                docFilesMapper.toDocumentFileData(file)
            } returns DocumentFile(
                fileId = file.fileId,
                name = file.name,
                mimeType = file.mimeType,
                uriString = file.uri.toString(),
                size = file.size,
                md5 = file.md5,
                isEdit = file.isEdit
            )
        }

        val actual = filesPersistenceManager.prepareEditedFilesToPersist(files)

        assertEquals(expectedProductImages, actual)
        files.forEach { image ->
            coVerify { docFilesMapper.toDocumentFileData(image) }
        }
    }
}
