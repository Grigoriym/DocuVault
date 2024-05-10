package com.grappim.docuvault.feature.group.repoimpl.di

import com.grappim.docuvault.feature.group.repoapi.mappers.GroupFieldMapper
import com.grappim.docuvault.feature.group.repoapi.mappers.GroupMapper
import com.grappim.docuvault.feature.group.repoimpl.mappers.GroupFieldMapperImpl
import com.grappim.docuvault.feature.group.repoimpl.mappers.GroupMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
interface GroupMapperModule {
    @Binds
    fun bindGroupMapper(groupMapperImpl: GroupMapperImpl): GroupMapper

    @Binds
    fun bindGroupFieldMapper(groupFieldMapperImpl: GroupFieldMapperImpl): GroupFieldMapper
}
