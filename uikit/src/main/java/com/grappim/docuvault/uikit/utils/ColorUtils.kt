package com.grappim.docuvault.uikit.utils

import androidx.compose.ui.graphics.Color

interface ColorUtils {
    fun toComposeColor(string: String): Color

    fun toHexString(color: Color): String
}
