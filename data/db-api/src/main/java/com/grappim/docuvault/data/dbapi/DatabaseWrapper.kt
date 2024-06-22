package com.grappim.docuvault.data.dbapi

import com.grappim.docuvault.data.backupdb.BackupFilesDao
import com.grappim.docuvault.feature.docs.db.dao.DocumentsDao
import com.grappim.docuvault.feature.group.db.dao.GroupsDao

interface DatabaseWrapper {

    val documentsDao: DocumentsDao
    val backupFilesDao: BackupFilesDao
    val groupsDao: GroupsDao
    suspend fun clearAllTables()

    fun close()
}
