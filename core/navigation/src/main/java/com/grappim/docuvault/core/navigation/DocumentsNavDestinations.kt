package com.grappim.docuvault.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

const val IS_FROM_EDIT = "is_from_edit"

@Serializable
data object DocsListNavRoute

@Serializable
data class DocDetailsNavRoute(val documentId: Long, val isFromEdit: Boolean = false)

@Serializable
data class DocManagerNavRoute(val documentId: Long? = null)

fun NavController.navigateToDocsList(navOptions: NavOptions) =
    navigate(route = DocsListNavRoute, navOptions)

fun NavController.navigateToDocDetails(
    documentId: Long,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = DocDetailsNavRoute(documentId)) {
        navOptions()
    }
}

fun NavController.navigateToDocManager(
    documentId: Long?,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = DocManagerNavRoute(documentId)) {
        navOptions()
    }
}
