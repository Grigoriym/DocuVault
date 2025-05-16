package com.grappim.docuvault.feature.docgroup.domain

data class GroupToCreate(
    val name: String,
    val fields: List<GroupField>,
    val color: String
)
