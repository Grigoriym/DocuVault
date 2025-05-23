package com.grappim.docuvault.utils.files.creation

import android.content.ContentResolver
import com.grappim.docuvault.testing.ShadowFileProvider
import com.grappim.docuvault.testing.getRandomString
import com.grappim.docuvault.testing.getRandomUri
import com.grappim.docuvault.utils.filesapi.creation.FileCreationUtils
import com.grappim.docuvault.utils.filesapi.inforetriever.FileInfoRetriever
import com.grappim.docuvault.utils.filesapi.pathmanager.FolderPathManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowContentResolver
import java.io.ByteArrayInputStream
import java.io.File
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(
    shadows = [ShadowFileProvider::class],
    manifest = Config.NONE
)
class FileCreationUtilsImplTest {

    private lateinit var fileCreationUtils: FileCreationUtils

    private val context = RuntimeEnvironment.getApplication()

    private val fileInfoRetriever: FileInfoRetriever = mockk()
    private val folderPathManager: FolderPathManager = mockk()

    private lateinit var contentResolver: ContentResolver
    private lateinit var shadowContentResolver: ShadowContentResolver

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        fileCreationUtils = FileCreationUtilsImpl(
            fileInfoRetriever = fileInfoRetriever,
            context = context,
            folderPathManager = folderPathManager,
            ioDispatcher = UnconfinedTestDispatcher()
        )

        contentResolver = context.contentResolver
        shadowContentResolver = shadowOf(contentResolver)
    }

    @Test
    fun `on createFileLocally should correctly create a new file from uri`() = runTest {
        val content = getRandomString().toByteArray()
        val fileDataByteArray = ByteArrayInputStream(content)
        val uri = getRandomUri()
        shadowContentResolver.registerInputStream(
            uri,
            fileDataByteArray
        )
        val folderName = "testFolder"
        val fileName = getRandomString()
        val extension = getRandomString()

        val mainFolderFile = File(context.filesDir, "products/$folderName").apply { mkdirs() }

        every { fileInfoRetriever.getFileExtension(any()) } returns extension
        every { folderPathManager.getMainFolder(any()) } returns mainFolderFile
        every { fileInfoRetriever.getFileName(extension = any()) } returns fileName

        val actual = fileCreationUtils.createFileLocally(uri, folderName)

        verify { fileInfoRetriever.getFileExtension(uri) }
        verify { folderPathManager.getMainFolder(folderName) }
        verify { fileInfoRetriever.getFileName(extension = extension) }

        val expected = File(context.filesDir, "products/$folderName/$fileName").apply {
            writeBytes(content)
        }

        assertEquals(expected, actual)
    }
}
