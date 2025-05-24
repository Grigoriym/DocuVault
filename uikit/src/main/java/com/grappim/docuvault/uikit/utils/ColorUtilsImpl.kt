package com.grappim.docuvault.uikit.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import javax.inject.Inject

class ColorUtilsImpl @Inject constructor() : ColorUtils {
    override fun toComposeColor(string: String): Color {
        val completeColorString = if (string.first() == '#') string else "#$string"
        return Color(completeColorString.toColorInt())
    }

    override fun toHexString(color: Color): String {
        return Integer.toHexString(color.toArgb())
    }
}
