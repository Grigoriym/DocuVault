package com.grappim.docsofmine.ui.screens.main.docs.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.grappim.docsofmine.model.document.DocumentListUI
import com.grappim.docsofmine.uikit.theme.DefaultArrangement
import com.grappim.docsofmine.uikit.theme.DefaultHorizontalPadding
import com.grappim.docsofmine.uikit.theme.DocsOfMineTheme

@Composable
fun DocsScreen(
    viewModel: DocsViewModel = hiltViewModel(),
    onDocumentClick: (id: String) -> Unit
) {
    val documents by viewModel.documents.collectAsState()
    DocsScreenContent(
        documents = documents,
        onDocumentClick = onDocumentClick
    )
}

@Composable
private fun DocsScreenContent(
    documents: List<DocumentListUI>,
    onDocumentClick: (id: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DefaultHorizontalPadding),
        verticalArrangement = Arrangement.spacedBy(DefaultArrangement),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(documents) { document ->
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                backgroundColor = document.groupColor,
                onClick = {
                    onDocumentClick(document.id)
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
                        Image(
                            painter = rememberAsyncImagePainter(document.preview),
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun FilesScreenContentPreview() {
    DocsOfMineTheme {
        DocsScreenContent(
            documents = emptyList(),
            onDocumentClick = {}
        )
    }
}