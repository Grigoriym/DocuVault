package com.grappim.docuvault.ui.screens.main

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.grappim.docuvault.core.navigation.DocumentsNavDestinations
import com.grappim.docuvault.core.navigation.MainNavDestinations
import com.grappim.docuvault.core.navigation.SettingsNavDestinations
import com.grappim.docuvault.core.navigation.bottomNavigationScreens
import com.grappim.docuvault.feature.docs.navigation.docsScreen
import com.grappim.docuvault.feature.group.navigation.groupsScreens
import com.grappim.docuvault.ui.screens.settings.SettingsItem
import com.grappim.docuvault.ui.screens.settings.SettingsScreen
import com.grappim.docuvault.ui.screens.settings.about.AboutScreen
import com.grappim.docuvault.uikit.theme.DocuVaultTheme
import com.grappim.docuvault.uikit.widget.PlatoIcon

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    MainScreenContent(navController = navController)
}

@Composable
private fun MainScreenContent(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination
    val showBottomNavigation = bottomNavigationScreens.any { it.route == currentRoute?.route }

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
        bottomBar = {
            if (showBottomNavigation) {
                BottomAppBar(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp
                            )
                        ),
                    cutoutShape = CircleShape
                ) {
                    BottomNav(
                        navController = navController,
                        currentRoute = currentRoute
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            if (showBottomNavigation) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(
                            DocumentsNavDestinations.DocManager.getRouteToNavigate("")
                        ) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    shape = CircleShape
                ) {
                    PlatoIcon(imageVector = DocumentsNavDestinations.DocManager.icon)
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .padding(paddingValues),
            navController = navController,
            startDestination = MainNavDestinations.Settings.route
        ) {
            groupsScreens(navController)
            docsScreen(navController)

            composable(MainNavDestinations.Settings.route) {
                SettingsScreen(
                    onItemClick = {
                        when (it) {
                            is SettingsItem.About -> {
                                navController.navigate(SettingsNavDestinations.About.route)
                            }
                        }
                    }
                )
            }
            composable(SettingsNavDestinations.About.route) {
                AboutScreen()
            }
        }
    }
}

@Composable
private fun BottomNav(navController: NavController, currentRoute: NavDestination?) {
    BottomNavigation(
        modifier = Modifier,
        elevation = 0.dp
    ) {
        bottomNavigationScreens.forEach { screen ->
            BottomNavigationItem(
                selected = currentRoute?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(imageVector = screen.icon, contentDescription = "")
                },
                label = {
                    Text(text = screen.title)
                }
            )
        }
        Spacer(Modifier.weight(1f, true))
    }
}

@Composable
@Preview
private fun MainRootScreenContentPreview() {
    DocuVaultTheme {
        MainScreenContent(rememberNavController())
    }
}
