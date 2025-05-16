package com.grappim.docuvault.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.grappim.docuvault.uikit.theme.DocuVaultTheme
import com.grappim.docuvault.uikit.widget.PlatoDrawer
import com.grappim.docuvault.uikit.widget.PlatoFab
import com.grappim.docuvault.uikit.widget.PlatoTopAppBar
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

    PlatoDrawer(
        screens = appState.topLevelDestinations,
        currentItem = appState.currentTopLevelDestination,
        drawerState = drawerState,
        onDrawerItemClicked = { item ->
            scope.launch {
                drawerState.close()
            }
            appState.navigateToTopLevelDestination(item)
        }
    ) {
        Scaffold(
            topBar = {
                PlatoTopAppBar(
                    currentTitle = appState.appBarTitle,
                    onNavigationClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
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
            }
        ) { paddingValues ->
            MainNavHost(
                modifier = Modifier.padding(paddingValues),
                mainAppState = appState
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
