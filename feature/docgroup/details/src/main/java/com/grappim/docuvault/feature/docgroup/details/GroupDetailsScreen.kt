package com.grappim.docuvault.feature.docgroup.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.grappim.docuvault.uikit.theme.DefaultArrangement
import com.grappim.docuvault.uikit.theme.DefaultHorizontalPadding
import com.grappim.docuvault.uikit.widget.PlatoImage

@Composable
fun GroupDetailsRoute(
    viewModel: GroupDetailsViewModel = hiltViewModel(),
    onDocumentClick: (documentId: Long) -> Unit
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    if (state.group != null) {
        GroupDetailsContent(state = state, onDocumentClick = onDocumentClick)
    }
}

@Composable
private fun GroupDetailsContent(state: GroupDetailsState, onDocumentClick: (id: Long) -> Unit) {
    requireNotNull(state.group)

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DefaultHorizontalPadding),
        verticalArrangement = Arrangement.spacedBy(DefaultArrangement),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(text = state.group.name)
        }
        items(state.documents) { document ->
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors().copy(
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
