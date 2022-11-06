package com.grappim.docsofmine.data.di

import com.grappim.docsofmine.data.storage.GoogleDrivePrefsImpl
import com.grappim.domain.storage.GoogleDrivePrefs
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
interface StorageBindsModule {

    @Binds
    fun bindGoogleDrivePrefs(
        googleDrivePrefsImpl: GoogleDrivePrefsImpl
    ): GoogleDrivePrefs
}