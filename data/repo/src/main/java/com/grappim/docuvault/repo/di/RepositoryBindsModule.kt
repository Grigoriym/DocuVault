package com.grappim.docuvault.repo.di

import com.grappim.docuvault.repo.DocumentRepositoryImpl
import com.grappim.domain.repository.DocumentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
interface RepositoryBindsModule {
    @Binds
    fun bindDocumentRepository(documentRepositoryImpl: DocumentRepositoryImpl): DocumentRepository
}
