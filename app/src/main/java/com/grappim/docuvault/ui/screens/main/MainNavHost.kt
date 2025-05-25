package com.grappim.docuvault.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.grappim.docuvault.core.navigation.destinations.GROUP_IS_FROM_EDIT
import com.grappim.docuvault.core.navigation.destinations.IS_FROM_EDIT
import com.grappim.docuvault.core.navigation.destinations.navigateToDocDetails
import com.grappim.docuvault.core.navigation.destinations.navigateToDocManager
import com.grappim.docuvault.core.navigation.destinations.navigateToGroupDetails
import com.grappim.docuvault.core.navigation.destinations.navigateToGroupManager
import com.grappim.docuvault.feature.docgroup.navigation.groupsScreens
import com.grappim.docuvault.feature.docs.navigation.docsScreen
import com.grappim.docuvault.feature.settings.navigation.settingsScreens

@Composable
fun MainNavHost(
    modifier: Modifier,
    mainAppState: MainAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    val navController = mainAppState.navController
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = mainAppState.globalStartDestination
    ) {
        /**
         * When navigating back from the manager, we want to update the doc values which are shown
         * otherwise the previous screen won't demonstrate any change if there was any
         */
        fun handleDocBackNavigation(isNewDoc: Boolean) {
            if (!isNewDoc) {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(IS_FROM_EDIT, true)
            }
            navController.popBackStack()
        }

        docsScreen(
            onDocumentClick = navController::navigateToDocDetails,
            onEditClicked = navController::navigateToDocManager,
            onDocumentDone = ::handleDocBackNavigation,
            goBackFromDocManager = ::handleDocBackNavigation
        )

        /**
         * When navigating back from the manager, we want to update the group values which are shown
         * otherwise the previous screen won't demonstrate any change if there was any
         */
        fun handleGroupBackNavigation(isNewDoc: Boolean) {
            if (!isNewDoc) {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(GROUP_IS_FROM_EDIT, true)
            }
            navController.popBackStack()
        }

        groupsScreens(
            onGroupClick = navController::navigateToGroupDetails,
            onDocClickedFromGroup = navController::navigateToDocDetails,
            onBackFromManager = ::handleGroupBackNavigation,
            onBackFromDetails = navController::popBackStack,
            onShowSnackbar = onShowSnackbar,
            onEditClicked = navController::navigateToGroupManager,
            onGroupSaved = ::handleGroupBackNavigation
        )

        settingsScreens()
    }
}
