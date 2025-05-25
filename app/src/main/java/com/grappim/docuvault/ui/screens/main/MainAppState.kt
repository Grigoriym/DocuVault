package com.grappim.docuvault.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.grappim.docuvault.core.navigation.Destination
import com.grappim.docuvault.core.navigation.DrawerDestination
import com.grappim.docuvault.core.navigation.NormalDestination
import com.grappim.docuvault.core.navigation.destinations.DocsListNavRoute
import com.grappim.docuvault.core.navigation.destinations.navigateToDocManager
import com.grappim.docuvault.core.navigation.destinations.navigateToDocsList
import com.grappim.docuvault.core.navigation.destinations.navigateToGroupManager
import com.grappim.docuvault.core.navigation.destinations.navigateToGroupsList
import com.grappim.docuvault.core.navigation.destinations.navigateToSettings
import com.grappim.docuvault.uikit.widget.PlatoTopAppBarState

@Composable
fun rememberMainAppState(navController: NavHostController = rememberNavController()): MainAppState {
    return remember(navController) {
        MainAppState(navController)
    }
}

@Stable
class MainAppState(
    val navController: NavHostController
) {

    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val globalStartDestination = DocsListNavRoute

    private val currentDestination: NavDestination?
        @Composable get() {
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val currentTopLevelDestination: DrawerDestination?
        @Composable get() = DrawerDestination.entries.firstOrNull { drawerDestination ->
            currentDestination?.hasRoute(route = drawerDestination.route) == true
        }

    private val allScreens: List<Destination> = listOf(
        DrawerDestination.DocsList,
        DrawerDestination.GroupsList,
        DrawerDestination.Settings,
        NormalDestination.DocDetails,
        NormalDestination.GroupDetails,
        NormalDestination.DocManager,
        NormalDestination.GroupManager
    )

    val appBarTitle: String
        @Composable get() {
            val currentDestination = allScreens.firstOrNull { d ->
                currentDestination?.hasRoute(route = d.route) == true
            }
            return currentDestination?.title ?: ""
        }

    val topLevelDestinations = DrawerDestination.entries

    val topAppBarState: PlatoTopAppBarState
        @Composable get() {
            val isTopLevelDest = topLevelDestinations.any { dest ->
                currentDestination?.hasRoute(route = dest.route) == true
            }
            return if (isTopLevelDest) {
                PlatoTopAppBarState.WithDrawable
            } else {
                PlatoTopAppBarState.WithBackButton
            }
        }

    private val fabDestinations = listOf(
        DrawerDestination.GroupsList,
        DrawerDestination.DocsList
    )

    val isFabEnabled: Boolean
        @Composable get() = fabDestinations.any { drawerDestinations ->
            currentDestination?.hasRoute(route = drawerDestinations.route) == true
        }

    @Composable
    fun NavigateFromFab() {
        if (!isFabEnabled) return
        val currentFabScreen = fabDestinations.firstOrNull { drawerDestinations ->
            currentDestination?.hasRoute(route = drawerDestinations.route) == true
        }
        when (currentFabScreen) {
            DrawerDestination.DocsList -> {
                navController.navigateToDocManager(null)
            }

            DrawerDestination.GroupsList -> {
                navController.navigateToGroupManager()
            }

            else -> {
            }
        }
    }

    fun navigateToTopLevelDestination(destination: DrawerDestination) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        when (destination) {
            DrawerDestination.DocsList -> navController.navigateToDocsList(navOptions)

            DrawerDestination.GroupsList -> navController.navigateToGroupsList(navOptions)

            DrawerDestination.Settings -> navController.navigateToSettings(navOptions)
        }
    }
}
