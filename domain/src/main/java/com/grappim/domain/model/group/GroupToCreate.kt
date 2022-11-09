package com.grappim.domain.model.group

data class GroupToCreate(
    val name: String,
    val fields: List<GroupField>,
    val color: String
)