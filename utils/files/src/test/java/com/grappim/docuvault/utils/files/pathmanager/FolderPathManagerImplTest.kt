package com.grappim.docuvault.utils.files.pathmanager

import com.grappim.docuvault.utils.filesapi.pathmanager.FolderPathManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.io.File
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE
)
class FolderPathManagerImplTest {

    private lateinit var sut: FolderPathManager

    private val context = RuntimeEnvironment.getApplication()

    @Before
    fun setUp() {
        sut = FolderPathManagerImpl(
            context = context
        )
    }

    @Test
    fun `getMainFolder should return correct folder`() {
        val expected = File(context.filesDir, "docufiles")

        val actual = sut.getMainFolder()

        assertEquals(expected.path, actual.path)
    }

    @Test
    fun `getMainFolder with a child should return correct folder`() {
        val expected = File(context.filesDir, "docufiles/testChild")

        val actual = sut.getMainFolder("testChild")

        assertEquals(expected.path, actual.path)
    }

    @Test
    fun `getTempFolderName should return correct temp folder string`() {
        val expected = "testFolder_temp"
        val actual = sut.getTempFolderName("testFolder")
        assertEquals(expected, actual)
    }

    @Test
    fun `getBackupFolderName should return correct backup folder string`() {
        val expected = "testFolder_backup"
        val actual = sut.getBackupFolderName("testFolder")
        assertEquals(expected, actual)
    }
}
