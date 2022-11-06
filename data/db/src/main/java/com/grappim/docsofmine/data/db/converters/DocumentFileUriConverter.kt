package com.grappim.docsofmine.data.db.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.grappim.docsofmine.data.db.model.DocumentFileUriDTO
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@[ProvidedTypeConverter Singleton]
class DocumentFileUriConverter @Inject constructor(
    private val json: Json
) {

    @TypeConverter
    fun fromList(list: List<DocumentFileUriDTO>): String =
        if (list.isEmpty()) {
            ""
        } else {
            json.encodeToString(list)
        }

    @TypeConverter
    fun toList(string: String): List<DocumentFileUriDTO> =
        if (string.isEmpty()) {
            emptyList()
        } else {
            json.decodeFromString(string)
        }

}