package com.grappim.docuvault.feature.docgroup.repoapi.model

data class Group(
    val id: Long,
    val name: String,
    val fields: List<GroupField>,
    val color: String
)
