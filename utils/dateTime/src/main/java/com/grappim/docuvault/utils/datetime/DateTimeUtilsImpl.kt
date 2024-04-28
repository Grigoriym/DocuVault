package com.grappim.docuvault.utils.datetime

import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateTimeUtilsImpl @Inject constructor(
    @DtfToStore private val dtfToStore: DateTimeFormatter,
    @DtfToDemonstrate private val dtfToDemonstrate: DateTimeFormatter,
    @DtfGDriveDocumentFolder private val dtfGDriveDocumentFolder: DateTimeFormatter,
    @DtfGDriveRfc3339 private val dtfGDriveRfc3339: DateTimeFormatter
) : DateTimeUtils {
    override fun formatToStore(offsetDateTime: OffsetDateTime): String =
        dtfToStore.format(offsetDateTime)

    override fun parseToStore(string: String): OffsetDateTime =
        OffsetDateTime.from(dtfToStore.parse(string))

    override fun formatToDemonstrate(offsetDateTime: OffsetDateTime, inUtc: Boolean): String =
        if (inUtc) {
            val createdDateInCurrentOffset = getLocalTimeFromUTC(offsetDateTime)
            dtfToDemonstrate.format(createdDateInCurrentOffset)
        } else {
            dtfToDemonstrate.format(offsetDateTime)
        }

    override fun formatToGDrive(offsetDateTime: OffsetDateTime): String = dtfGDriveDocumentFolder
        .format(offsetDateTime)

    override fun getDateFromGDrive(date: String): OffsetDateTime =
        OffsetDateTime.from(dtfGDriveRfc3339.parse(date))
            .withOffsetSameLocal(OffsetDateTime.now().offset)

    override fun getDateTimeUTCNow(): OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)

    override fun getLocalTimeFromUTC(offsetDateTime: OffsetDateTime): OffsetDateTime =
        offsetDateTime.withOffsetSameInstant(OffsetDateTime.now().offset)
}
