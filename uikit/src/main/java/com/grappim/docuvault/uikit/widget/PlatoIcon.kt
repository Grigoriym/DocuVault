package com.grappim.docuvault.uikit.widget

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun PlatoIcon(modifier: Modifier = Modifier, imageVector: ImageVector) {
    Icon(
        modifier = modifier,
        imageVector = imageVector,
        contentDescription = imageVector.name
    )
}

@Composable
fun PlatoIcon(modifier: Modifier = Modifier, painter: Painter) {
    Icon(
        modifier = modifier,
        painter = painter,
        contentDescription = painter.toString()
    )
}
