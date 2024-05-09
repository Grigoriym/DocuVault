package com.grappim.docuvault.core.navigation

sealed interface GroupNavDestinations {
    val route: String

    data object CreateGroup : GroupNavDestinations {
        override val route: String = "group_create_group_destination"
    }
}
