package com.grappim.docuvault.feature.docs.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.grappim.docuvault.uikit.theme.DefaultArrangement
import com.grappim.docuvault.uikit.theme.DefaultHorizontalPadding
import com.grappim.docuvault.uikit.theme.DocuVaultTheme
import com.grappim.docuvault.uikit.utils.ThemePreviews
import com.grappim.docuvault.uikit.widget.PlatoImage

@Composable
fun DocsScreen(viewModel: DocsViewModel = hiltViewModel(), onDocumentClick: (id: Long) -> Unit) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    DocsScreenContent(
        state = state,
        onDocumentClick = onDocumentClick
    )
}

@Composable
private fun DocsScreenContent(state: DocsListState, onDocumentClick: (id: Long) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = DefaultHorizontalPadding),
        verticalArrangement = Arrangement.spacedBy(DefaultArrangement),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(state.documents) { document ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onDocumentClick(document.id.toLong())
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {
                    Row {
                        Column {
                            Text(
                                text = document.name,
                                modifier = Modifier
                                    .padding(
                                        top = 8.dp
                                    )
                            )
                            Text(text = document.createdDate)
                        }
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(DefaultArrangement)
                    ) {
                        items(document.preview) { preview ->
                            Card(
                                modifier = Modifier
                                    .padding(
                                        top = 8.dp,
                                        bottom = 8.dp
                                    )
                                    .size(100.dp)
                            ) {
                                PlatoImage(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = rememberAsyncImagePainter(preview),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@[Composable ThemePreviews]
private fun FilesScreenContentPreview() {
    DocuVaultTheme {
        DocsScreenContent(
            state = DocsListState(),
            onDocumentClick = {}
        )
    }
}
