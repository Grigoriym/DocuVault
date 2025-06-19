package com.grappim.docuvault.data.backupimpl

import com.grappim.docuvault.data.backupapi.BackupFilesRepository
import com.grappim.docuvault.data.dbapi.DatabaseWrapper
import com.grappim.docuvault.feature.docs.repoapi.models.DocumentFile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupFilesRepositoryImpl @Inject constructor(
    private val databaseWrapper: DatabaseWrapper,
    private val backupFileMapper: BackupFileMapper
) : BackupFilesRepository {
    override suspend fun insertFiles(documentId: Long, files: List<DocumentFile>) {
        val entities = backupFileMapper.toBackupDocumentFileDataEntity(documentId, files)
        databaseWrapper.backupFilesDao.insert(entities)
    }

    override suspend fun deleteFiles(documentId: Long, files: List<DocumentFile>) {
        val entities = backupFileMapper.toBackupDocumentFileDataEntity(documentId, files)
        databaseWrapper.backupFilesDao.delete(entities)
    }

    override suspend fun deleteFilesByDocumentId(documentId: Long) {
        databaseWrapper.backupFilesDao.deleteFilesByDocumentId(documentId)
    }

    override suspend fun getAllByDocumentId(documentId: Long): List<DocumentFile> {
        val result = databaseWrapper.backupFilesDao.getAllFilesByDocumentId(documentId)
        return backupFileMapper.toDocumentFileData(result)
    }
}
