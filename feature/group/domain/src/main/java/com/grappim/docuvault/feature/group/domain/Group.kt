package com.grappim.docuvault.feature.group.domain

data class Group(
    val id: Long,
    val name: String,
    val fields: List<GroupField>,
    val color: String
)
