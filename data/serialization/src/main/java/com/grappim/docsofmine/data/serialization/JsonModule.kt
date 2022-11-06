package com.grappim.docsofmine.data.serialization

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
object JsonModule {

    @[Provides Singleton]
    fun provideJson():Json =
        Json {
            isLenient = true
            prettyPrint = false
            ignoreUnknownKeys = true
            explicitNulls = false
        }
}