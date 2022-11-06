package com.grappim.docsofmine.data.di

import com.grappim.docsofmine.data.repository.DocumentRepositoryImpl
import com.grappim.docsofmine.data.repository.GroupRepositoryImpl
import com.grappim.domain.repository.DocumentRepository
import com.grappim.domain.repository.GroupRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
interface RepositoryBindsModule {

    @Binds
    fun bindGroupRepository(
        categoryRepositoryImpl: GroupRepositoryImpl
    ): GroupRepository

    @Binds
    fun bindDocumentRepository(
        documentRepositoryImpl: DocumentRepositoryImpl
    ): DocumentRepository
}