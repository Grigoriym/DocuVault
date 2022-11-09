package com.grappim.docsofmine.data.db.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.grappim.docsofmine.data.db.model.group.GroupFieldEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@ProvidedTypeConverter
@Singleton
class GroupFieldConverter @Inject constructor(
    private val json: Json
) {

    @TypeConverter
    fun fromCategoryFieldList(list: List<GroupFieldEntity>): String =
        if (list.isEmpty()) {
            ""
        } else {
            json.encodeToString(list)
        }

    @TypeConverter
    fun toCategoryFieldList(list: String): List<GroupFieldEntity> =
        if (list.isEmpty()) {
            emptyList()
        } else {
            json.decodeFromString(list)
        }

}