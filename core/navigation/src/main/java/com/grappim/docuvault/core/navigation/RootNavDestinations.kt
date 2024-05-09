package com.grappim.docuvault.core.navigation

sealed interface RootNavDestinations {
    val route: String

    data object HomeDestination : RootNavDestinations {
        override val route: String = "home_destination"
    }
}
