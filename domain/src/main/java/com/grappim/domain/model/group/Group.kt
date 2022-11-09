package com.grappim.domain.model.group

data class Group(
    val id: Long,
    val name: String,
    val fields: List<GroupField>,
    val color: String
) {
    companion object {
        fun getGroupsForPreview() = listOf(
            Group(id = 0, name = "Personal", fields = listOf(), color = "98D9C2"),
            Group(id = 0, name = "Bank", fields = listOf(), color = "68C3D4"),
            Group(id = 0, name = "Invoice", fields = listOf(), color = "FFB30F")
        )

        fun getGroupForPreview() =
            Group(id = 0, name = "Personal", fields = listOf(), color = "98D9C2")
    }
}