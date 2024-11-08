package com.grappim.docuvault.data.db

import com.grappim.docuvault.common.async.IoDispatcher
import com.grappim.docuvault.data.backupdb.BackupFilesDao
import com.grappim.docuvault.data.dbapi.DatabaseWrapper
import com.grappim.docuvault.feature.docs.db.dao.DocumentsDao
import com.grappim.docuvault.feature.group.db.dao.GroupsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseWrapperImpl @Inject constructor(
    private val db: DocuVaultDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DatabaseWrapper {
    override val documentsDao: DocumentsDao
        get() = db.documentsDao()
    override val backupFilesDao: BackupFilesDao
        get() = db.backupFilesDao()
    override val groupsDao: GroupsDao
        get() = db.groupsDao()

    override suspend fun clearAllTables() = withContext(ioDispatcher) {
        db.clearAllTables()
    }

    override fun close() {
        db.close()
    }
}
