package com.grappim.docuvault.core.navigation

import com.grappim.docuvault.core.navigation.destinations.DocDetailsNavRoute
import com.grappim.docuvault.core.navigation.destinations.DocManagerNavRoute
import com.grappim.docuvault.core.navigation.destinations.GroupDetailsNavRoute
import com.grappim.docuvault.core.navigation.destinations.GroupManagerNavRoute
import kotlin.reflect.KClass

enum class NormalDestination(
    override val title: String,
    override val route: KClass<*>
) : Destination {
    DocDetails(
        title = "Details",
        route = DocDetailsNavRoute::class
    ),
    DocManager(
        title = "Manager",
        route = DocManagerNavRoute::class
    ),
    GroupDetails(
        title = "Details",
        route = GroupDetailsNavRoute::class
    ),
    GroupManager(
        title = "Manager",
        route = GroupManagerNavRoute::class
    )
}
