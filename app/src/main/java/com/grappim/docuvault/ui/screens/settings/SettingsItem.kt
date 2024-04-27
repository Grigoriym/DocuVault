package com.grappim.docuvault.ui.screens.settings

sealed interface SettingsItem {
    val name: String

    data class About(
        override val name: String = "About"
    ) : SettingsItem
}
