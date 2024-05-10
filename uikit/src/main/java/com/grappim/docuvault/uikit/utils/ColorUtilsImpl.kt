package com.grappim.docuvault.uikit.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import javax.inject.Inject

class ColorUtilsImpl @Inject constructor() : ColorUtils {
    override fun toComposeColor(string: String): Color {
        val completeColorString = if (string.first() == '#') string else "#$string"
        return Color(android.graphics.Color.parseColor(completeColorString))
    }

    override fun toHexString(color: Color): String {
        return Integer.toHexString(color.toArgb())
    }
}
