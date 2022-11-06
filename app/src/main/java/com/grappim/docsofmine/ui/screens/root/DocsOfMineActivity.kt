package com.grappim.docsofmine.ui.screens.root

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.ktx.AppUpdateResult
import com.grappim.docsofmine.core.inAppUpdate.rememberInAppUpdateState
import com.grappim.docsofmine.navigation.RootNavDestinations
import com.grappim.docsofmine.ui.screens.main.MainScreen
import com.grappim.docsofmine.uikit.theme.DocsOfMineTheme
import com.grappim.docsofmine.uikit.widget.DomSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DocsOfMineActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DocsOfMineTheme {
                RootScreen()
            }
        }
    }

    @Composable
    private fun RootScreen() {
        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()
        val inAppUpdateState = rememberInAppUpdateState()

        LaunchedEffect(inAppUpdateState) {
            if (inAppUpdateState.appUpdateResult is AppUpdateResult.Downloaded) {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = "New version downloaded",
                    actionLabel = "Install"
                )

                if ((result) == SnackbarResult.ActionPerformed) {
                    (inAppUpdateState.appUpdateResult as AppUpdateResult.Downloaded).completeUpdate()
                }
            }
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            snackbarHost = {
                SnackbarHost(it) { data ->
                    DomSnackbar(
                        snackbarData = data
                    )
                }
            },
            scaffoldState = scaffoldState
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = RootNavDestinations.HomeDestination.route,
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                composable(RootNavDestinations.HomeDestination.route) {
                    MainScreen()
                }
            }
        }
    }
}