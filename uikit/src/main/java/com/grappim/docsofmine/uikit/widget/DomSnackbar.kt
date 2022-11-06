package com.grappim.docsofmine.uikit.widget

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.grappim.docsofmine.uikit.theme.Dom_Aero

@Composable
fun DomSnackbar(
    snackbarData: SnackbarData
) {
    Snackbar(
        snackbarData = snackbarData,
        contentColor = Dom_Aero,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(8.dp)
    )
}