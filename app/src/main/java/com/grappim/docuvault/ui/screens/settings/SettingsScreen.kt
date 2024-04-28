package com.grappim.docuvault.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(onItemClick: (SettingsItem) -> Unit) {
    LazyColumn {
        items(
            listOf(SettingsItem.About())
        ) { item ->
            SettingsItem(
                item = item,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
private fun SettingsItem(item: SettingsItem, onItemClick: (SettingsItem) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {
            onItemClick(item)
        }
    ) {
        Column {
            Text(
                modifier = Modifier
                    .padding(
                        vertical = 12.dp,
                        horizontal = 10.dp
                    ),
                text = item.name
            )
            Divider(
                modifier = Modifier
                    .padding(top = 6.dp)
            )
        }
    }
}
