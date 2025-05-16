package com.grappim.docuvault.uikit.widget

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.grappim.docuvault.uikit.theme.Dom_Aero

@Composable
fun PlatoSnackbar(snackbarData: SnackbarData) {
    Snackbar(
        snackbarData = snackbarData,
        contentColor = Dom_Aero,
        shape = RoundedCornerShape(8.dp)
    )
}
