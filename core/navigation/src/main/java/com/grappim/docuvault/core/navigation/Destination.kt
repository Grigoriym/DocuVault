package com.grappim.docuvault.core.navigation

import kotlin.reflect.KClass

interface Destination {
    val title: String
    val route: KClass<*>
}
