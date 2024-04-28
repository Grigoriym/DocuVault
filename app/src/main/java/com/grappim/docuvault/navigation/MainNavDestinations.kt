package com.grappim.docuvault.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface MainNavDestinations {
    val route: String
    val icon: ImageVector
    val title: String

    data object Home : MainNavDestinations {
        override val route: String = "main_home_destination"
        override val icon: ImageVector = Icons.Filled.Home
        override val title: String = "Home"
    }

    data object Docs : MainNavDestinations {
        override val route: String = "main_docs_destination"
        override val icon: ImageVector = Icons.Filled.Description
        override val title: String = "Docs"
    }

    data object Groups : MainNavDestinations {
        override val route: String = "main_groups_destination"
        override val icon: ImageVector = Icons.Filled.Dataset
        override val title: String = "Groups"
    }

    data object Settings : MainNavDestinations {
        override val route: String = "main_settings_destination"
        override val icon: ImageVector = Icons.Filled.Settings
        override val title: String = "Settings"
    }

    data object Add : MainNavDestinations {
        override val route: String = "main_add_destination"
        override val icon: ImageVector = Icons.Filled.Add
        override val title: String = "Add"
    }
}

val bottomNavigationScreens: List<MainNavDestinations> = listOf(
    MainNavDestinations.Home,
    MainNavDestinations.Docs,
    MainNavDestinations.Groups,
    MainNavDestinations.Settings
)
