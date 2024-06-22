package com.grappim.docuvault.data.db.di

import android.content.Context
import androidx.room.Room
import com.grappim.docuvault.data.backupdb.BackupFilesDao
import com.grappim.docuvault.data.db.BuildConfig
import com.grappim.docuvault.data.db.DatabaseWrapperImpl
import com.grappim.docuvault.data.db.DocuVaultDatabase
import com.grappim.docuvault.data.db.converters.DateTimeConverter
import com.grappim.docuvault.data.dbapi.DatabaseWrapper
import com.grappim.docuvault.feature.docs.db.dao.DocumentsDao
import com.grappim.docuvault.feature.group.db.dao.GroupsDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
class DatabaseModule {

    @[Provides Singleton]
    fun provideRoomDatabase(
        @ApplicationContext context: Context,
        dateTimeConverter: DateTimeConverter
    ): DocuVaultDatabase = Room.databaseBuilder(
        context,
        DocuVaultDatabase::class.java,
        "docuvault_${BuildConfig.BUILD_TYPE}.db"
    )
        .addTypeConverter(dateTimeConverter)
        .createFromAsset("db/docuvault_prepoluate_${BuildConfig.BUILD_TYPE}.db")
        .build()

    @[Provides Singleton]
    fun provideCategoryDao(databaseWrapper: DatabaseWrapper): GroupsDao = databaseWrapper.groupsDao

    @[Provides Singleton]
    fun provideDocumentDao(databaseWrapper: DatabaseWrapper): DocumentsDao =
        databaseWrapper.documentsDao

    @[Provides Singleton]
    fun provideBackupFilesDao(databaseWrapper: DatabaseWrapper): BackupFilesDao =
        databaseWrapper.backupFilesDao
}

@[Module InstallIn(SingletonComponent::class)]
interface DatabaseBindsModule {
    @Binds
    fun bindDatabaseWrapper(databaseWrapperImpl: DatabaseWrapperImpl): DatabaseWrapper
}
