package com.grappim.docuvault.core.navigation

sealed interface SettingsNavDestinations {
    val route: String

    data object About : SettingsNavDestinations {
        override val route: String = "settings_about_destination"
    }
}
