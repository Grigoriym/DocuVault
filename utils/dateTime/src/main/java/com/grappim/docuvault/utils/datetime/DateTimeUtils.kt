package com.grappim.docuvault.utils.datetime

import java.time.OffsetDateTime

interface DateTimeUtils {
    fun formatToStore(offsetDateTime: OffsetDateTime): String

    fun parseToStore(string: String): OffsetDateTime

    fun formatToDemonstrate(offsetDateTime: OffsetDateTime, inUtc: Boolean = false): String

    fun formatToGDrive(offsetDateTime: OffsetDateTime): String

    fun getDateFromGDrive(date: String): OffsetDateTime

    fun getDateTimeUTCNow(): OffsetDateTime

    fun getLocalTimeFromUTC(offsetDateTime: OffsetDateTime): OffsetDateTime
}
