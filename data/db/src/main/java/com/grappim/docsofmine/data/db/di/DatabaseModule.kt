package com.grappim.docsofmine.data.db.di

import android.content.Context
import androidx.room.Room
import com.grappim.docsofmine.data.db.BuildConfig
import com.grappim.docsofmine.data.db.DocsOfMineDatabase
import com.grappim.docsofmine.data.db.converters.DateTimeConverter
import com.grappim.docsofmine.data.db.converters.GroupFieldConverter
import com.grappim.docsofmine.data.db.dao.DocumentsDao
import com.grappim.docsofmine.data.db.dao.GroupsDao
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
        groupFieldConverter: GroupFieldConverter,
        dateTimeConverter: DateTimeConverter
    ): DocsOfMineDatabase =
        Room.databaseBuilder(
            context,
            DocsOfMineDatabase::class.java,
            "docsofmine_${BuildConfig.BUILD_TYPE}.db"
        )
            .addTypeConverter(groupFieldConverter)
            .addTypeConverter(dateTimeConverter)
            .createFromAsset("db/docsofmine_prepoluate_${BuildConfig.BUILD_TYPE}.db")
            .build()

    @[Provides Singleton]
    fun provideCategoryDao(
        docsOfMineDatabase: DocsOfMineDatabase
    ): GroupsDao = docsOfMineDatabase.categoryDao()

    @[Provides Singleton]
    fun provideDocumentDao(
        docsOfMineDatabase: DocsOfMineDatabase
    ): DocumentsDao = docsOfMineDatabase.documentsDao()
}