package com.grappim.docuvault.data.dbapi

import com.grappim.docuvault.data.backupdb.BackupFilesDao
import com.grappim.docuvault.feature.docgroup.db.dao.GroupsDao
import com.grappim.docuvault.feature.docs.db.dao.DocumentsDao

interface DatabaseWrapper {

    val documentsDao: DocumentsDao
    val backupFilesDao: BackupFilesDao
    val groupsDao: GroupsDao
    suspend fun clearAllTables()

    fun close()
}
