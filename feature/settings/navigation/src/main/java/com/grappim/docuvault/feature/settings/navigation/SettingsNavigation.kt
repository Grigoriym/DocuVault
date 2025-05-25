package com.grappim.docuvault.feature.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.grappim.docuvault.core.navigation.destinations.SettingsNavRoute
import com.grappim.docuvault.settings.ui.SettingsScreen

fun NavGraphBuilder.settingsScreens() {
    composable<SettingsNavRoute> {
        SettingsScreen()
    }
}
