package com.grappim.docuvault.data.backupimpl

import com.grappim.docuvault.data.backupapi.BackupFilesRepository
import com.grappim.docuvault.data.backupdb.BackupFilesDao
import com.grappim.docuvault.feature.docs.domain.DocumentFile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupFilesRepositoryImpl @Inject constructor(
    private val backupFilesDao: BackupFilesDao,
    private val backupFileMapper: BackupFileMapper
) : BackupFilesRepository {
    override suspend fun insertFiles(documentId: Long, files: List<DocumentFile>) {
        val entities = backupFileMapper.toBackupDocumentFileDataEntity(documentId, files)
        backupFilesDao.insert(entities)
    }

    override suspend fun deleteFiles(documentId: Long, files: List<DocumentFile>) {
        val entities = backupFileMapper.toBackupDocumentFileDataEntity(documentId, files)
        backupFilesDao.delete(entities)
    }

    override suspend fun deleteFilesByDocumentId(documentId: Long) {
        backupFilesDao.deleteFilesByDocumentId(documentId)
    }

    override suspend fun getAllByDocumentId(documentId: Long): List<DocumentFile> {
        val result = backupFilesDao.getAllFilesByDocumentId(documentId)
        return backupFileMapper.toDocumentFileData(result)
    }
}
