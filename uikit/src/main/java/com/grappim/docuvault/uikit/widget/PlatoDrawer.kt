package com.grappim.docuvault.uikit.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grappim.docuvault.core.navigation.DrawerDestination

@Composable
fun PlatoDrawer(
    screens: List<DrawerDestination>,
    currentItem: DrawerDestination?,
    onDrawerItemClicked: (DrawerDestination) -> Unit,
    drawerState: DrawerState,
    gesturesEnabled: Boolean,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        gesturesEnabled = gesturesEnabled,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "DocuVault",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    LazyColumn {
                        items(screens) { item ->
                            NavigationDrawerItem(
                                label = { Text(item.title) },
                                selected = currentItem == item,
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = ""
                                    )
                                },
                                onClick = {
                                    onDrawerItemClicked(item)
                                }
                            )
                        }
                    }
                }
            }
        },
        content = content
    )
}
