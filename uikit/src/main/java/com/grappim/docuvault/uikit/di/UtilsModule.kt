package com.grappim.docuvault.uikit.di

import com.grappim.docuvault.uikit.utils.ColorUtils
import com.grappim.docuvault.uikit.utils.ColorUtilsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
interface UtilsModule {
    @Binds
    fun bindColorUtils(colorUtilsImpl: ColorUtilsImpl): ColorUtils
}
