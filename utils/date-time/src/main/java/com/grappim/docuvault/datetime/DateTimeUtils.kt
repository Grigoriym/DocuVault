package com.grappim.docuvault.datetime

import java.time.Instant
import java.time.OffsetDateTime

interface DateTimeUtils {
    fun formatToStore(offsetDateTime: OffsetDateTime): String

    fun parseToStore(string: String): OffsetDateTime

    fun formatToDemonstrate(offsetDateTime: OffsetDateTime): String

    fun formatToGDrive(offsetDateTime: OffsetDateTime): String

    fun getDateFromGDrive(date: String): OffsetDateTime

    fun getDateTimeUTCNow(): OffsetDateTime

    fun getLocalTimeFromUTC(offsetDateTime: OffsetDateTime): OffsetDateTime

    fun formatToDocumentFolder(offsetDateTime: OffsetDateTime): String

    fun getInstantNow(): Instant
}
