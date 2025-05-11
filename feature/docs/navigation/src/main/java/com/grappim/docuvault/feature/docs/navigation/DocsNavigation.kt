package com.grappim.docuvault.feature.docs.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.grappim.docuvault.core.navigation.DocDetailsNavRoute
import com.grappim.docuvault.core.navigation.DocManagerNavRoute
import com.grappim.docuvault.core.navigation.DocsListNavRoute
import com.grappim.docuvault.core.navigation.IS_FROM_EDIT
import com.grappim.docuvault.feature.docs.details.DocumentDetailsScreen
import com.grappim.docuvault.feature.docs.list.DocsScreen
import com.grappim.docuvault.feature.docs.manager.DocumentManagerRoute

fun NavGraphBuilder.docsScreen(
    onDocumentClick: (id: Long) -> Unit,
    onEditClicked: (documentId: Long) -> Unit,
    onDocumentDone: (isNewProduct: Boolean) -> Unit,
    goBackFromDocManager: (isNewProduct: Boolean) -> Unit
) {
    composable<DocsListNavRoute> {
        DocsScreen(
            onDocumentClick = onDocumentClick
        )
    }

    composable<DocDetailsNavRoute> { navBackStackEntry ->
        fun NavBackStackEntry.getIsFromEdit(defaultValue: Boolean = false): Boolean {
            return this.savedStateHandle
                .get<Boolean>(IS_FROM_EDIT)
                ?: defaultValue
        }

        val isFromEdit = navBackStackEntry.getIsFromEdit(false)
        DocumentDetailsScreen(
            isFromEdit = isFromEdit,
            onEditClicked = onEditClicked
        )
    }

    composable<DocManagerNavRoute> {
        DocumentManagerRoute(
            onDocumentDone = onDocumentDone,
            goBack = goBackFromDocManager
        )
    }
}
