package com.grappim.docsofmine.navigation

sealed interface SettingsNavDestinations {
    val route: String

    object About : SettingsNavDestinations {
        override val route: String = "settings_about_destination"
    }

    object GoogleSync : SettingsNavDestinations {
        override val route: String = "settings_google_sync_destination"
    }
}