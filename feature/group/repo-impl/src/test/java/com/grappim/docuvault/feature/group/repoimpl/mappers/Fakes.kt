package com.grappim.docuvault.feature.group.repoimpl.mappers

import com.grappim.docuvault.feature.group.db.model.GroupEntity
import com.grappim.docuvault.feature.group.db.model.GroupFieldEntity
import com.grappim.docuvault.feature.group.db.model.GroupWithFieldsEntity
import com.grappim.docuvault.feature.group.domain.Group
import com.grappim.docuvault.feature.group.domain.GroupField
import com.grappim.docuvault.feature.group.domain.GroupToCreate
import com.grappim.docuvault.testing.getRandomLong
import com.grappim.docuvault.testing.getRandomString

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

fun getGroupToCreate(): GroupToCreate = GroupToCreate(
    name = getRandomString(),
    color = getRandomString(),
    fields = listOf(getGroupField(), getGroupField())
)
