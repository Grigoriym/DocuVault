package com.grappim.docuvault.core.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
data object SettingsNavRoute

fun NavController.navigateToSettings(navOptions: NavOptions) =
    navigate(route = SettingsNavRoute, navOptions)
