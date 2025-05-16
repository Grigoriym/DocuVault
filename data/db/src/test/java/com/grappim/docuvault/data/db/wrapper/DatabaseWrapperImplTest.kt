package com.grappim.docuvault.data.db.wrapper

import com.grappim.docuvault.data.db.DatabaseWrapperImpl
import com.grappim.docuvault.data.db.DocuVaultDatabase
import com.grappim.docuvault.data.dbapi.DatabaseWrapper
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DatabaseWrapperImplTest {
    private val db = mockk<DocuVaultDatabase>(relaxed = true)

    private lateinit var wrapper: DatabaseWrapper

    @Before
    fun setup() {
        wrapper = DatabaseWrapperImpl(
            db = db,
            ioDispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `documentsDao should call db productsDao`() {
        wrapper.documentsDao

        verify { db.documentsDao() }
    }

    @Test
    fun `backupFilesDao should call db databaseDao`() {
        wrapper.backupFilesDao

        verify { db.backupFilesDao() }
    }

    @Test
    fun `groupsDao should call db backupImagesDao`() {
        wrapper.groupsDao

        verify { db.groupsDao() }
    }

    @Test
    fun `clearAllTables should call db clearAllTables`() = runTest {
        wrapper.clearAllTables()

        verify { db.clearAllTables() }
    }

    @Test
    fun `close should call db close`() = runTest {
        wrapper.close()

        verify { db.close() }
    }
}
