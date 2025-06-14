package com.grappim.docuvault.data.db.di

import android.content.Context
import androidx.room.Room
import com.grappim.docuvault.data.db.BuildConfig
import com.grappim.docuvault.data.db.DatabaseWrapperImpl
import com.grappim.docuvault.data.db.DocuVaultDatabase
import com.grappim.docuvault.data.db.converters.DateTimeConverter
import com.grappim.docuvault.data.dbapi.DatabaseWrapper
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
}

@[Module InstallIn(SingletonComponent::class)]
interface DatabaseBindsModule {
    @Binds
    fun bindDatabaseWrapper(databaseWrapperImpl: DatabaseWrapperImpl): DatabaseWrapper
}
