package com.grappim.docsofmine.ui.screens.main

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grappim.docsofmine.navigation.DocumentsNavDestinations
import com.grappim.docsofmine.navigation.GroupNavDestinations
import com.grappim.docsofmine.navigation.MainNavDestinations
import com.grappim.docsofmine.navigation.SettingsNavDestinations
import com.grappim.docsofmine.navigation.bottomNavigationScreens
import com.grappim.docsofmine.ui.screens.main.docs.add.AddDocumentScreen
import com.grappim.docsofmine.ui.screens.main.docs.details.DocumentDetailsScreen
import com.grappim.docsofmine.ui.screens.main.docs.list.DocsScreen
import com.grappim.docsofmine.ui.screens.main.groups.GroupsScreen
import com.grappim.docsofmine.ui.screens.main.groups.create.CreateGroupScreen
import com.grappim.docsofmine.ui.screens.main.home.HomeScreen
import com.grappim.docsofmine.ui.screens.main.settings.SettingsItem
import com.grappim.docsofmine.ui.screens.main.settings.SettingsScreen
import com.grappim.docsofmine.ui.screens.main.settings.about.AboutScreen
import com.grappim.docsofmine.ui.screens.main.settings.drive.GoogleDriveScreen
import com.grappim.docsofmine.uikit.theme.DocsOfMineTheme

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    MainScreenContent(navController = navController)
}

@Composable
private fun MainScreenContent(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination
    val showBottomNavigation = bottomNavigationScreens.any { it.route == currentRoute?.route }

    var fabState by remember {
        mutableStateOf(FabState.COLLAPSED)
    }
    val fabTransition = updateTransition(targetState = fabState, label = "fabTransition")
    val fabRotation by fabTransition.animateFloat(label = "fabRotation") { state ->
        if (state == FabState.EXPANDED) 45f else 0f
    }

    Scaffold(
        modifier = Modifier
            .statusBarsPadding(),
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
                        navController.navigate(MainNavDestinations.Add.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = MainNavDestinations.Add.icon,
                        contentDescription = "",
                        modifier = Modifier
                            .rotate(fabRotation)
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .padding(paddingValues),
            navController = navController,
            startDestination = MainNavDestinations.Home.route
        ) {
            composable(MainNavDestinations.Home.route) {
                HomeScreen()
            }
            composable(MainNavDestinations.Docs.route) {
                DocsScreen(
                    onDocumentClick = {
                        navController.navigate(
                            DocumentsNavDestinations.Details
                                .createRoute(it.id.toString())
                        )
                    }
                )
            }
            composable(MainNavDestinations.Groups.route) {
                GroupsScreen(
                    onCreateGroupClick = {
                        navController.navigate(GroupNavDestinations.CreateGroup.route)
                    }
                )
            }
            composable(MainNavDestinations.Add.route) {
                AddDocumentScreen(
                    onDocumentCreated = {
                        navController.popBackStack()
                    },
                    goBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(MainNavDestinations.Settings.route) {
                SettingsScreen(
                    onItemClick = {
                        when (it) {
                            is SettingsItem.About -> {
                                navController.navigate(SettingsNavDestinations.About.route)
                            }
                            is SettingsItem.GoogleSync -> {
                                navController.navigate(SettingsNavDestinations.GoogleSync.route)
                            }
                        }
                    }
                )
            }
            composable(GroupNavDestinations.CreateGroup.route) {
                CreateGroupScreen()
            }
            composable(SettingsNavDestinations.About.route) {
                AboutScreen()
            }
            composable(
                route = DocumentsNavDestinations.Details.route,
                arguments = listOf(navArgument(DocumentsNavDestinations.Details.documentIdArgument()) {
                    type = NavType.StringType
                })
            ) {
                DocumentDetailsScreen()
            }
            composable(SettingsNavDestinations.GoogleSync.route) {
                GoogleDriveScreen()
            }
        }
    }
}

@Composable
private fun BottomNav(
    navController: NavController,
    currentRoute: NavDestination?
) {
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
    DocsOfMineTheme {
        MainScreenContent(rememberNavController())
    }
}