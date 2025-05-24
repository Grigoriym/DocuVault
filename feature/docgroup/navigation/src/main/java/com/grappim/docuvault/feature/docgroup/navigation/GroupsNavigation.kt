package com.grappim.docuvault.feature.docgroup.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.grappim.docuvault.core.navigation.GROUP_IS_FROM_EDIT
import com.grappim.docuvault.core.navigation.GroupDetailsNavRoute
import com.grappim.docuvault.core.navigation.GroupManagerNavRoute
import com.grappim.docuvault.core.navigation.GroupsListNavRoute
import com.grappim.docuvault.feature.docgroup.details.GroupDetailsRoute
import com.grappim.docuvault.feature.docgroup.list.GroupListScreenRoute
import com.grappim.docuvault.feature.docgroup.manager.GroupManagerScreenRoute

fun NavGraphBuilder.groupsScreens(
    onGroupClick: (Long) -> Unit,
    onDocClickedFromGroup: (Long) -> Unit,
    onBackFromManager: (isNewProduct: Boolean) -> Unit,
    onGroupSaved: (isNewProduct: Boolean) -> Unit,
    onBackFromDetails: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onEditClicked: (id: Long) -> Unit
) {
    composable<GroupsListNavRoute> {
        GroupListScreenRoute(
            onGroupClick = onGroupClick
        )
    }
    composable<GroupManagerNavRoute> {
        GroupManagerScreenRoute(
            onBack = onBackFromManager,
            onShowSnackbar = onShowSnackbar,
            onGroupSaved = onGroupSaved
        )
    }
    composable<GroupDetailsNavRoute> { navBackStackEntry ->
        fun NavBackStackEntry.getIsFromEdit(defaultValue: Boolean = false): Boolean {
            return this.savedStateHandle
                .get<Boolean>(GROUP_IS_FROM_EDIT)
                ?: defaultValue
        }

        val isFromEdit = navBackStackEntry.getIsFromEdit(false)

        GroupDetailsRoute(
            onDocumentClick = onDocClickedFromGroup,
            onEditClicked = onEditClicked,
            goBack = onBackFromDetails,
            isFromEdit = isFromEdit
        )
    }
}
