package com.grappim.docuvault.feature.group.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.grappim.docuvault.core.navigation.GroupNavDestinations
import com.grappim.docuvault.core.navigation.MainNavDestinations
import com.grappim.docuvault.feature.group.details.GroupDetailsRoute
import com.grappim.docuvault.feature.group.list.GroupListScreenRoute
import com.grappim.docuvault.feature.group.manager.GroupManagerScreenRoute

fun NavGraphBuilder.groupsScreens(navController: NavController) {
    composable(MainNavDestinations.GroupList.route) {
        GroupListScreenRoute(
            onCreateGroupClick = {
                navController.navigate(GroupNavDestinations.GroupManager.route)
            },
            onGroupClick = {
                navController.navigate(
                    GroupNavDestinations.GroupDetails.getRouteToNavigate(
                        it.toString()
                    )
                )
            }
        )
    }
    composable(GroupNavDestinations.GroupManager.route) {
        GroupManagerScreenRoute(
            onGroupSaved = {
                navController.popBackStack()
            }
        )
    }
    composable(
        route = GroupNavDestinations.GroupDetails.route,
        arguments = listOf(
            navArgument(GroupNavDestinations.GroupDetails.KEY_GROUP_ID) {
                type = NavType.StringType
                nullable = false
            }
        )
    ) {
        GroupDetailsRoute(
            goBack = {
                navController.popBackStack()
            }
        )
    }
}
