package com.grappim.docuvault.core.navigation

import kotlin.reflect.KClass

interface Destination {
    val title: String
    val route: KClass<*>
}

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
