package com.grappim.docuvault.feature.docgroup.repoapi.model

data class GroupToCreate(
    val name: String,
    val fields: List<GroupField>,
    val color: String
)
