package com.grappim.docsofmine.navigation

sealed interface RootNavDestinations {
    val route: String

    object HomeDestination : RootNavDestinations {
        override val route: String = "home_destination"
    }
}