package com.grappim.docuvault.uikit.widget

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun PlatoFab(isVisible: Boolean, onFabClicked: @Composable () -> Unit) {
    var clickFab by remember { mutableStateOf(false) }
    if (clickFab) {
        onFabClicked()
        clickFab = false
    }
    if (isVisible) {
        FloatingActionButton(
            onClick = {
                clickFab = true
            },
            shape = CircleShape
        ) {
            PlatoIcon(imageVector = Icons.Filled.Add)
        }
    }
}
