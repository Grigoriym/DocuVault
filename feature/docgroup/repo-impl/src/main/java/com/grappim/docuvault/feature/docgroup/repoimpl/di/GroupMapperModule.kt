package com.grappim.docuvault.feature.docgroup.repoimpl.di

import com.grappim.docuvault.feature.docgroup.repoapi.mappers.GroupFieldMapper
import com.grappim.docuvault.feature.docgroup.repoapi.mappers.GroupMapper
import com.grappim.docuvault.feature.docgroup.repoimpl.mappers.GroupFieldMapperImpl
import com.grappim.docuvault.feature.docgroup.repoimpl.mappers.GroupMapperImpl
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
