package com.grappim.docuvault.data.backupimpl

import com.grappim.docuvault.data.backupapi.BackupFilesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
interface BackupModule {
    @Binds
    fun bindBackupRepository(
        backupFilesRepositoryImpl: BackupFilesRepositoryImpl
    ): BackupFilesRepository
}
