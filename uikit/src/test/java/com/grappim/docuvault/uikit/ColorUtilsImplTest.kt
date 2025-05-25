package com.grappim.docuvault.uikit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import com.grappim.docuvault.uikit.utils.ColorUtils
import com.grappim.docuvault.uikit.utils.ColorUtilsImpl
import kotlin.test.Test
import kotlin.test.assertEquals

class ColorUtilsImplTest {
    private val sut: ColorUtils = ColorUtilsImpl()

    @Test
    fun `on toComposeColor returns compose color`() {
        val colorString = "#FFFF00FF"
        val expected = Color(colorString.toColorInt())

        val actual = sut.toComposeColor(colorString)

        assertEquals(expected, actual)
    }

    @Test
    fun `on toHexString returns hex string`() {
        val color = Color.Red
        val expected = Integer.toHexString(color.toArgb())

        val actual = sut.toHexString(color)

        assertEquals(expected, actual)
    }
}
