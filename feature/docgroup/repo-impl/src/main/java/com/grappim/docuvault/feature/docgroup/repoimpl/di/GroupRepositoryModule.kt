package com.grappim.docuvault.feature.docgroup.repoimpl.di

import com.grappim.docuvault.feature.docgroup.repoapi.GroupRepository
import com.grappim.docuvault.feature.docgroup.repoimpl.GroupRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
interface GroupRepositoryModule {
    @Binds
    fun bindGroupRepository(categoryRepositoryImpl: GroupRepositoryImpl): GroupRepository
}
