package com.grappim.docuvault.uikit.widget

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

@Composable
fun PlatoImage(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center
) {
    Image(
        modifier = modifier,
        painter = painter,
        contentScale = contentScale,
        contentDescription = painter.toString(),
        alignment = alignment
    )
}
