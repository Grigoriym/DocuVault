package com.grappim.docuvault.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

@Serializable
data object GroupManagerNavRoute

@Serializable
data class GroupDetailsNavRoute(val groupId: Long)

@Serializable
data object GroupsListNavRoute

fun NavController.navigateToGroupsList(navOptions: NavOptions) =
    navigate(route = GroupsListNavRoute, navOptions)

fun NavController.navigateToGroupManager(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = GroupManagerNavRoute) { navOptions() }
}

fun NavController.navigateToGroupDetails(
    groupId: Long,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = GroupDetailsNavRoute(groupId)) {
        navOptions()
    }
}
