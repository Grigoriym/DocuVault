package com.grappim.docuvault.data.cleanerimpl

import com.grappim.docuvault.data.cleanerapi.DataCleaner
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
interface DataCleanerModule {
    @Binds
    fun bindDataCleanerModule(dataCleaner: DataCleanerImpl): DataCleaner
}
