package com.grappim.docuvault.data.db.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.grappim.docuvault.utils.files.datetime.DateTimeUtils
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@[ProvidedTypeConverter Singleton]
class DateTimeConverter @Inject constructor(private val dateTimeUtils: DateTimeUtils) {
    @TypeConverter
    fun fromDateTime(offsetDateTime: OffsetDateTime): String =
        dateTimeUtils.formatToStore(offsetDateTime)

    @TypeConverter
    fun toDateTime(string: String): OffsetDateTime = dateTimeUtils.parseToStore(string)
}
