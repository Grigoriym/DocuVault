package com.grappim.docuvault.testing

import com.grappim.docuvault.feature.docgroup.db.model.GroupEntity
import com.grappim.docuvault.feature.docgroup.db.model.GroupFieldEntity
import com.grappim.docuvault.feature.docgroup.db.model.GroupWithFieldsEntity
import com.grappim.docuvault.feature.docgroup.domain.Group
import com.grappim.docuvault.feature.docgroup.domain.GroupField
import com.grappim.docuvault.feature.docgroup.domain.GroupToCreate

fun getGroupWithFieldsEntity(): GroupWithFieldsEntity {
    val groupId = getRandomLong()
    return GroupWithFieldsEntity(
        groupEntity = GroupEntity(
            groupId = groupId,
            name = getRandomString(),
            color = getRandomString()
        ),
        groupFields = listOf(getGroupFieldEntity(groupId), getGroupFieldEntity(groupId))
    )
}

fun getGroupFieldEntity(groupId: Long = getRandomLong()) = GroupFieldEntity(
    groupFieldId = getRandomLong(),
    name = getRandomString(),
    value = getRandomString(),
    groupId = groupId
)

fun getGroup(): Group {
    val id = getRandomLong()

    return Group(
        id = id,
        name = getRandomString(),
        fields = listOf(getGroupField(id), getGroupField(id)),
        color = getRandomString()
    )
}

fun getGroupField(groupId: Long = getRandomLong()): GroupField = GroupField(
    groupId = groupId,
    name = getRandomString(),
    value = getRandomString()
)

fun getGroupToCreate(newFields: List<GroupField>? = null): GroupToCreate = GroupToCreate(
    name = getRandomString(),
    color = getRandomString(),
    fields = newFields ?: listOf(getGroupField(), getGroupField())
)

fun getGroupEntity(): GroupEntity = GroupEntity(
    groupId = getRandomLong(),
    name = getRandomString(),
    color = getRandomString()
)
