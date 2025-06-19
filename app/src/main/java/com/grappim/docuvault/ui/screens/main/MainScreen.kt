package com.grappim.docuvault.ui.screens.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.grappim.docuvault.uikit.theme.DocuVaultTheme
import com.grappim.docuvault.uikit.widget.PlatoDrawer
import com.grappim.docuvault.uikit.widget.PlatoFab
import com.grappim.docuvault.uikit.widget.PlatoTopAppBar
import com.grappim.docuvault.uikit.widget.PlatoTopAppBarState
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    MainScreenContent()
}

@Composable
private fun MainScreenContent() {
    val appState = rememberMainAppState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    PlatoDrawer(
        screens = appState.topLevelDestinations,
        currentItem = appState.currentTopLevelDestination,
        drawerState = drawerState,
        onDrawerItemClicked = { item ->
            scope.launch {
                drawerState.close()
            }
            appState.navigateToTopLevelDestination(item)
        },
        gesturesEnabled = appState.topAppBarState is PlatoTopAppBarState.WithDrawable
    ) {
        Scaffold(
            modifier = Modifier
                .imePadding(),
            topBar = {
                PlatoTopAppBar(
                    currentTitle = appState.appBarTitle,
                    onNavigationClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onBackClicked = {
                        appState.navController.popBackStack()
                    },
                    state = appState.topAppBarState
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                PlatoFab(
                    isVisible = appState.isFabEnabled,
                    onFabClicked = {
                        appState.NavigateFromFab()
                    }
                )
            },
            snackbarHost = {
                SnackbarHost(
                    snackbarHostState,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
                )
            }
        ) { paddingValues ->
            MainNavHost(
                modifier = Modifier.padding(paddingValues),
                mainAppState = appState,
                onShowSnackbar = { message, action ->
                    snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = action,
                        duration = SnackbarDuration.Short
                    ) == SnackbarResult.ActionPerformed
                }
            )
        }
    }
}

@Composable
@Preview
private fun MainRootScreenContentPreview() {
    DocuVaultTheme {
        MainScreenContent()
    }
}
