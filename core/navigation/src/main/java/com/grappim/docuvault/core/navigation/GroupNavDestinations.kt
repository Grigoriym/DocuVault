package com.grappim.docuvault.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

const val GROUP_IS_FROM_EDIT = "group_is_from_edit"

@Serializable
/**
 * groupId can be null when we create a new group
 * groupId cannot be null when we edit an existing group
 */
data class GroupManagerNavRoute(val groupId: Long? = null)

@Serializable
data class GroupDetailsNavRoute(val groupId: Long)

@Serializable
data object GroupsListNavRoute

fun NavController.navigateToGroupsList(navOptions: NavOptions) =
    navigate(route = GroupsListNavRoute, navOptions)

fun NavController.navigateToGroupManager(
    groupId: Long? = null,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = GroupManagerNavRoute(groupId)) { navOptions() }
}

fun NavController.navigateToGroupDetails(
    groupId: Long,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = GroupDetailsNavRoute(groupId)) {
        navOptions()
    }
}
