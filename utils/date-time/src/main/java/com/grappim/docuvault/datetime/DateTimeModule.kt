package com.grappim.docuvault.datetime

import com.grappim.docuvault.utils.datetimeapi.DateTimeUtils
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.format.DateTimeFormatter
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class DtfToStore

@Qualifier
annotation class DtfToDemonstrate

@Qualifier
annotation class DtfGDriveDocumentFolder

@Qualifier
annotation class DtfGDriveRfc3339

@Qualifier
annotation class DtfDocumentFolder

@[Module InstallIn(SingletonComponent::class)]
object DateTimeModule {
    private const val GOOGLE_DRIVE_DOCUMENT_FOLDER = "yyyy-MM-dd_HH-mm-ss"
    private const val PATTERN_TO_DEMONSTRATE = "yyyy.MM.dd HH:mm:ss"
    private const val DOCUMENT_FOLDER = "yyyy-MM-dd_HH-mm-ss"

    @[Provides Singleton DtfToStore]
    fun provideDtfToStore(): DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME

    @[Provides Singleton DtfToDemonstrate]
    fun provideDtfToDemonstrate(): DateTimeFormatter = DateTimeFormatter
        .ofPattern(PATTERN_TO_DEMONSTRATE)

    @[Provides Singleton DtfGDriveDocumentFolder]
    fun provideDtfGDriveDocumentFolder(): DateTimeFormatter =
        DateTimeFormatter.ofPattern(GOOGLE_DRIVE_DOCUMENT_FOLDER)

    @[Provides Singleton DtfGDriveRfc3339]
    fun provideDtfRfc3339(): DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @[Provides Singleton DtfDocumentFolder]
    fun provideDtfDocumentFolder(): DateTimeFormatter = DateTimeFormatter.ofPattern(DOCUMENT_FOLDER)
}

@[Module InstallIn(SingletonComponent::class)]
interface DateTimeBindsModule {
    @Binds
    fun bindDateTimeUtils(dateTimeUtils: DateTimeUtilsImpl): DateTimeUtils
}
