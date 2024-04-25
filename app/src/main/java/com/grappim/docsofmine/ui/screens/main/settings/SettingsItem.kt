package com.grappim.docsofmine.ui.screens.main.settings

sealed interface SettingsItem {
    val name: String

    data class About(
        override val name: String = "About"
    ) : SettingsItem
}
