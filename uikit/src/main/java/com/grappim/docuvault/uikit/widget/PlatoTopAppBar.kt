package com.grappim.docuvault.uikit.widget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatoTopAppBar(
    currentTitle: String,
    onNavigationClick: () -> Unit,
    state: PlatoTopAppBarState,
    onBackClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Text(
                currentTitle,
                maxLines = 1
            )
        },
        navigationIcon = {
            when (state) {
                PlatoTopAppBarState.WithDrawable -> {
                    IconButton(
                        onClick = {
                            onNavigationClick()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }

                PlatoTopAppBarState.WithBackButton -> {
                    IconButton(
                        onClick = {
                            onBackClicked()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Arrow Back"
                        )
                    }
                }
            }
        }
    )
}

sealed interface PlatoTopAppBarState {
    object WithDrawable : PlatoTopAppBarState
    object WithBackButton : PlatoTopAppBarState
}
