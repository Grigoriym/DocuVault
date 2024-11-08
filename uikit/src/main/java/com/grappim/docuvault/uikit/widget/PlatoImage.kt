package com.grappim.docuvault.uikit.widget

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

@Composable
fun PlatoImage(painter: Painter, contentScale: ContentScale = ContentScale.Fit) {
    Image(painter = painter, contentScale = contentScale, contentDescription = painter.toString())
}
