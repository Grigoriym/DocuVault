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
import com.grappim.domain.Document
import com.grappim.docsofmine.uikit.theme.DefaultArrangement
import com.grappim.docsofmine.uikit.theme.DefaultHorizontalPadding
import com.grappim.docsofmine.uikit.theme.DocsOfMineTheme
import com.grappim.docsofmine.uikit.utils.toColor

@Composable
fun DocsScreen(
    viewModel: DocsViewModel = hiltViewModel(),
    onDocumentClick: (Document) -> Unit
) {
    val documents by viewModel.documents.collectAsState()
    DocsScreenContent(
        documents = documents,
        onDocumentClick = onDocumentClick
    )
}

@Composable
private fun DocsScreenContent(
    documents: List<Document>,
    onDocumentClick: (Document) -> Unit
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
                backgroundColor = document.group.color.toColor(),
                onClick = {
                    onDocumentClick(document)
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
                    Text(text = document.createdDateString)

                    Card(
                        modifier = Modifier
                            .padding(
                                top = 8.dp,
                                bottom = 8.dp
                            )
                            .size(100.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(document.filesUri.first().string),
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
            documents = listOf(
                Document.getForPreview()
            ),
            onDocumentClick = {}
        )
    }
}