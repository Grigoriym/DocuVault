package com.grappim.docuvault.core.navigation

sealed interface GroupNavDestinations {
    val route: String

    data object GroupManager : GroupNavDestinations {
        override val route: String = "group_manager_destination"
    }

    data object GroupDetails : GroupNavDestinations {
        private const val PREFIX = "group_details_destination"

        const val KEY_GROUP_ID = "keyGroupId"
        override val route: String = "$PREFIX/?$KEY_GROUP_ID={$KEY_GROUP_ID}"
        fun getRouteToNavigate(id: String) = "$PREFIX/?$KEY_GROUP_ID=$id"
    }
}
