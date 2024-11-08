package com.grappim.docuvault.feature.docs.repoimpl.di

import com.grappim.docuvault.feature.docs.repoapi.mappers.DocumentFileMapper
import com.grappim.docuvault.feature.docs.repoapi.mappers.DocumentMapper
import com.grappim.docuvault.feature.docs.repoimpl.mappers.DocumentFileMapperImpl
import com.grappim.docuvault.feature.docs.repoimpl.mappers.DocumentMapperImpl
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
