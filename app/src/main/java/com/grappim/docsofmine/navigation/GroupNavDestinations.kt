package com.grappim.docsofmine.navigation

sealed interface GroupNavDestinations {
    val route: String

    object CreateGroup : GroupNavDestinations {
        override val route: String = "group_create_group_destination"
    }
}