package com.grappim.docuvault.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface MainNavDestinations {
    val route: String
    val icon: ImageVector
    val title: String

    data object Docs : MainNavDestinations {
        override val route: String = "main_docs_destination"
        override val icon: ImageVector = Icons.Filled.Description
        override val title: String = "Docs"
    }

    data object GroupList : MainNavDestinations {
        override val route: String = "main_groups_destination"
        override val icon: ImageVector = Icons.Filled.Dataset
        override val title: String = "Groups"
    }

    data object Settings : MainNavDestinations {
        override val route: String = "main_settings_destination"
        override val icon: ImageVector = Icons.Filled.Settings
        override val title: String = "Settings"
    }
}

val bottomNavigationScreens: List<MainNavDestinations> = listOf(
    MainNavDestinations.Docs,
    MainNavDestinations.GroupList,
    MainNavDestinations.Settings
)
