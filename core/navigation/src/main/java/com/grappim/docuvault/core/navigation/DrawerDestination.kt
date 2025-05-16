package com.grappim.docuvault.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.reflect.KClass

enum class DrawerDestination(
    val icon: ImageVector,
    override val title: String,
    override val route: KClass<*>
) : Destination {
    DocsList(
        icon = Icons.Filled.Description,
        title = "Docs",
        route = DocsListNavRoute::class
    ),
    GroupsList(
        icon = Icons.Filled.Dataset,
        title = "Groups",
        route = GroupsListNavRoute::class
    ),
    Settings(
        icon = Icons.Filled.Settings,
        title = "Settings",
        route = SettingsNavRoute::class
    )
}
