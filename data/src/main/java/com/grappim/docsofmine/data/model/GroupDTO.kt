package com.grappim.docsofmine.data.model

@kotlinx.serialization.Serializable
data class GroupDTO(
    val id: Long,
    val name: String,
    val fields: List<GroupFieldDTO>,
    val color: String
)
