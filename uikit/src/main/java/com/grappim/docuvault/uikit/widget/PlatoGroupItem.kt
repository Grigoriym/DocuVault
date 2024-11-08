package com.grappim.docuvault.uikit.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.grappim.docuvault.uikit.theme.DocuVaultTheme
import com.grappim.docuvault.uikit.utils.ThemePreviews

@Composable
fun PlatoGroupItem(
    color: Color,
    id: Long,
    name: String,
    isSelected: Boolean,
    onGroupClick: (id: Long) -> Unit
) {
    val cardColor = if (isSelected) {
        color
    } else {
        Color.Gray
    }
    Card(
        modifier = Modifier.size(100.dp),
        backgroundColor = cardColor,
        onClick = {
            onGroupClick(id)
        }
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(text = name)
        }
    }
}

@[Composable ThemePreviews]
private fun PlatoGroupItemPreview() {
    DocuVaultTheme {
        PlatoGroupItem(
            color = Color.Green,
            id = 1L,
            name = "name",
            isSelected = true,
            onGroupClick = {}
        )
    }
}

@[Composable ThemePreviews]
private fun PlatoGroupItemNotSelectedPreview() {
    DocuVaultTheme {
        PlatoGroupItem(
            color = Color.Blue,
            id = 1L,
            name = "name",
            isSelected = false,
            onGroupClick = {}
        )
    }
}
