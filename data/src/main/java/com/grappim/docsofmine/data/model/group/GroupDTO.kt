package com.grappim.docsofmine.data.model.group

@kotlinx.serialization.Serializable
data class GroupDTO(
    val id: Long,
    val name: String,
    val fields: List<GroupFieldDTO>,
    val color: String
)
