package com.grappim.docuvault.feature.docs.repoimpl.di

import com.grappim.docuvault.feature.docs.repoapi.DocumentRepository
import com.grappim.docuvault.feature.docs.repoimpl.DocumentRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
interface RepositoryBindsModule {
    @Binds
    fun bindDocumentRepository(documentRepositoryImpl: DocumentRepositoryImpl): DocumentRepository
}
