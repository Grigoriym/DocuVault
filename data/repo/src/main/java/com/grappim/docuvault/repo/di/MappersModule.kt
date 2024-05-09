package com.grappim.docuvault.repo.di

import com.grappim.docuvault.repo.mappers.DocumentFileMapper
import com.grappim.docuvault.repo.mappers.DocumentFileMapperImpl
import com.grappim.docuvault.repo.mappers.DocumentMapper
import com.grappim.docuvault.repo.mappers.DocumentMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
interface MappersModule {
    @Binds
    fun bindDocumentFileMapper(documentFileMapperImpl: DocumentFileMapperImpl): DocumentFileMapper

    @Binds
    fun bindDocumentMapper(documentMapperImpl: DocumentMapperImpl): DocumentMapper
}
