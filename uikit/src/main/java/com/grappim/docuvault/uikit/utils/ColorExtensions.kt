package com.grappim.docuvault.uikit.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun String.toColor(): Color = Color(android.graphics.Color.parseColor("#$this"))

fun Color.toDomString(): String = Integer.toHexString(toArgb())
