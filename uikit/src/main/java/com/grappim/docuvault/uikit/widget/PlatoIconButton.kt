package com.grappim.docuvault.uikit.widget

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.grappim.docuvault.uikit.theme.DocuVaultTheme
import com.grappim.docuvault.uikit.utils.ThemePreviews

@Composable
fun PlatoIconButton(modifier: Modifier = Modifier, icon: ImageVector, onButtonClick: () -> Unit) {
    IconButton(
        modifier = modifier
            .size(40.dp)
            .testTag(icon.name),
        onClick = onButtonClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            imageVector = icon,
            contentDescription = ""
        )
    }
}

@[Composable ThemePreviews]
fun PlatoIconButtonPreview() {
    DocuVaultTheme {
        PlatoIconButton(
            icon = Icons.Filled.Edit,
            onButtonClick = {}
        )
    }
}
