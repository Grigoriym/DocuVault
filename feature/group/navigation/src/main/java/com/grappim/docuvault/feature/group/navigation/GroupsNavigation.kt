package com.grappim.docuvault.feature.group.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.grappim.docuvault.core.navigation.GroupDetailsNavRoute
import com.grappim.docuvault.core.navigation.GroupManagerNavRoute
import com.grappim.docuvault.core.navigation.GroupsListNavRoute
import com.grappim.docuvault.feature.group.details.GroupDetailsRoute
import com.grappim.docuvault.feature.group.list.GroupListScreenRoute
import com.grappim.docuvault.feature.group.manager.GroupManagerScreenRoute

fun NavGraphBuilder.groupsScreens(
    onGroupClick: (Long) -> Unit,
    onDocClickedFromGroup: (Long) -> Unit,
    onBackFromManager: () -> Unit
) {
    composable<GroupsListNavRoute> {
        GroupListScreenRoute(
            onGroupClick = onGroupClick
        )
    }
    composable<GroupManagerNavRoute> {
        GroupManagerScreenRoute(
            onBack = onBackFromManager
        )
    }
    composable<GroupDetailsNavRoute> {
        GroupDetailsRoute(
            onDocumentClick = onDocClickedFromGroup
        )
    }
}
