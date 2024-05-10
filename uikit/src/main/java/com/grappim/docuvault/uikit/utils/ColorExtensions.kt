package com.grappim.docuvault.uikit.utils

import androidx.compose.ui.graphics.Color

@Deprecated("use colorUtils instead")
fun String.toComposeColor(): Color = Color(android.graphics.Color.parseColor("#$this"))
