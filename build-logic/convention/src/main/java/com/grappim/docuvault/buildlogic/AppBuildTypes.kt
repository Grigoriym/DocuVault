package com.grappim.docuvault.buildlogic

enum class AppBuildTypes(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}
