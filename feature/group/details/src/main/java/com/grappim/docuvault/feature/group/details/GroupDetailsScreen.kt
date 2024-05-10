package com.grappim.docuvault.feature.group.details

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
@Suppress("UnusedParameter")
fun GroupDetailsRoute(viewModel: GroupDetailsViewModel = hiltViewModel(), goBack: () -> Unit) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    GroupDetailsContent(state = state)
}

@Composable
private fun GroupDetailsContent(state: GroupDetailsState) {
    if (state.group != null) {
        Text(text = state.group.name)
    }
}
