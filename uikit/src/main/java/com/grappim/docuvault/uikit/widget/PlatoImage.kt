package com.grappim.docuvault.uikit.widget

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun PlatoImage(painter: Painter) {
    Image(painter = painter, contentDescription = painter.toString())
}
