package com.grappim.docuvault.feature.docgroup.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.grappim.docuvault.uikit.theme.DefaultArrangement
import com.grappim.docuvault.uikit.theme.DefaultHorizontalPadding
import com.grappim.docuvault.uikit.widget.PlatoAlertDialog
import com.grappim.docuvault.uikit.widget.PlatoImage

@Composable
fun GroupDetailsRoute(
    viewModel: GroupDetailsViewModel = hiltViewModel(),
    onDocumentClick: (documentId: Long) -> Unit,
    onEditClicked: (id: Long) -> Unit,
    goBack: () -> Unit,
    isFromEdit: Boolean
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    LaunchedEffect(state.groupDeleted) {
        if (state.groupDeleted) {
            goBack()
        }
    }

    LaunchedEffect(isFromEdit) {
        if (isFromEdit) {
            state.updateGroup()
        }
    }

    PlatoAlertDialog(
        text = stringResource(R.string.are_you_sure_to_delete_group),
        showAlertDialog = state.showDeletionDialog,
        dismissButtonText = stringResource(R.string.no),
        onDismissRequest = {
            state.onShowAlertDialog(false)
        },
        onConfirmButtonClicked = {
            state.onDeleteGroupConfirm()
        },
        onDismissButtonClicked = {
            state.onShowAlertDialog(false)
        }
    )

    if (state.group != null) {
        GroupDetailsContent(
            state = state,
            onDocumentClick = onDocumentClick,
            onEditClicked = onEditClicked
        )
    }
}

@Composable
private fun GroupDetailsContent(
    state: GroupDetailsState,
    onDocumentClick: (id: Long) -> Unit,
    onEditClicked: (id: Long) -> Unit
) {
    requireNotNull(state.group)

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DefaultHorizontalPadding),
        verticalArrangement = Arrangement.spacedBy(DefaultArrangement),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = state.group.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    onEditClicked(state.group.id)
                }, content = {
                    Text("Edit")
                })
                Button(onClick = state.onDeleteClicked, content = {
                    Text("Delete")
                })
            }
        }
        items(state.documents) { document ->
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = document.groupColor
                ),
                onClick = {
                    onDocumentClick(document.id.toLong())
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = document.name,
                        modifier = Modifier
                            .padding(
                                top = 8.dp
                            )
                    )
                    Text(text = document.createdDate)

                    Card(
                        modifier = Modifier
                            .padding(
                                top = 8.dp,
                                bottom = 8.dp
                            )
                            .size(100.dp)
                    ) {
                        PlatoImage(painter = rememberAsyncImagePainter(document.preview))
                    }
                }
            }
        }
    }
}
