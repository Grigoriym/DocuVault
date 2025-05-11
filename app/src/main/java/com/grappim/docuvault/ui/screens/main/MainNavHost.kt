package com.grappim.docuvault.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.grappim.docuvault.core.navigation.IS_FROM_EDIT
import com.grappim.docuvault.core.navigation.navigateToDocDetails
import com.grappim.docuvault.core.navigation.navigateToDocManager
import com.grappim.docuvault.core.navigation.navigateToGroupDetails
import com.grappim.docuvault.feature.docs.navigation.docsScreen
import com.grappim.docuvault.feature.group.navigation.groupsScreens
import com.grappim.docuvault.feature.settings.navigation.settingsScreens

@Composable
fun MainNavHost(modifier: Modifier, mainAppState: MainAppState) {
    val navController = mainAppState.navController
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = mainAppState.globalStartDestination
    ) {
        fun handleBackNavigation(isNewProduct: Boolean) {
            if (!isNewProduct) {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(IS_FROM_EDIT, true)
            }
            navController.popBackStack()
        }

        docsScreen(
            onDocumentClick = navController::navigateToDocDetails,
            onEditClicked = navController::navigateToDocManager,
            onDocumentDone = ::handleBackNavigation,
            goBackFromDocManager = ::handleBackNavigation
        )

        groupsScreens(
            onGroupClick = navController::navigateToGroupDetails,
            onDocClickedFromGroup = navController::navigateToDocDetails,
            onBackFromManager = navController::popBackStack
        )

        settingsScreens()
    }
}
