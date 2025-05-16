package com.grappim.docuvault.feature.docgroup.uiimpl

import com.grappim.docuvault.feature.docgroup.uiapi.GroupUIMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
interface Module {
    @Binds
    fun bindGroupUIMapper(groupUIMapperImpl: GroupUIMapperImpl): GroupUIMapper
}
