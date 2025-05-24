package com.grappim.docuvault.uikit.widget

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun PlatoIcon(imageVector: ImageVector) {
    Icon(
        imageVector = imageVector,
        contentDescription = imageVector.name
    )
}

@Composable
fun PlatoIcon(painter: Painter) {
    Icon(
        painter = painter,
        contentDescription = painter.toString()
    )
}
