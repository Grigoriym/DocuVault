package com.grappim.docuvault.utils.androidimpl.di

import com.grappim.docuvault.utils.androidapi.intent.IntentGenerator
import com.grappim.docuvault.utils.androidimpl.intent.IntentGeneratorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
interface AndroidUtilsModule {
    @Binds
    fun bindIntentGenerator(impl: IntentGeneratorImpl): IntentGenerator
}
