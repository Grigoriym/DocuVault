package com.grappim.docuvault.feature.docs.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.grappim.docuvault.core.navigation.DocumentsNavDestinations
import com.grappim.docuvault.core.navigation.MainNavDestinations
import com.grappim.docuvault.feature.docs.details.DocumentDetailsScreen
import com.grappim.docuvault.feature.docs.list.DocsScreen
import com.grappim.docuvault.feature.docs.manager.DocumentManagerRoute

fun NavGraphBuilder.docsScreen(navController: NavController) {
    composable(
        route = DocumentsNavDestinations.Details.route,
        arguments = listOf(
            navArgument(DocumentsNavDestinations.Details.KEY_DOC_ID) {
                type = NavType.LongType
            }
        )
    ) { navBackStackEntry ->
        fun NavBackStackEntry.getIsFromEdit(defaultValue: Boolean = false): Boolean {
            return this.savedStateHandle
                .get<Boolean>(DocumentsNavDestinations.Details.IS_FROM_EDIT)
                ?: defaultValue
        }

        val isFromEdit = navBackStackEntry.getIsFromEdit(false)

        DocumentDetailsScreen(
            onEditClicked = { id ->
                navController.navigate(
                    DocumentsNavDestinations.DocManager.getRouteToNavigate(id.toString())
                )
            },
            isFromEdit = isFromEdit
        )
    }
    composable(
        route = DocumentsNavDestinations.DocManager.route,
        arguments = listOf(
            navArgument(DocumentsNavDestinations.DocManager.KEY_EDIT_DOC_ID) {
                type = NavType.StringType
                nullable = true
            }
        )
    ) { navBackStackEntry ->
        fun handleBackNavigation(isNewProduct: Boolean) {
            if (!isNewProduct) {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(DocumentsNavDestinations.Details.IS_FROM_EDIT, true)
            }
            navController.popBackStack()
        }
        DocumentManagerRoute(
            onDocumentDone = { isNewProduct: Boolean ->
                handleBackNavigation(isNewProduct)
            },
            goBack = { isNewProduct: Boolean ->
                handleBackNavigation(isNewProduct)
            }
        )
    }
    composable(MainNavDestinations.Docs.route) {
        DocsScreen(
            onDocumentClick = { id ->
                navController.navigate(
                    DocumentsNavDestinations.Details.createRoute(id)
                )
            }
        )
    }
}
