package com.grappim.docsofmine.utils

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.grappim.docsofmine.uikit.theme.DefaultHorizontalPadding
import com.grappim.docsofmine.utils.files.FileUris
import com.grappim.domain.DocumentFileUri

@Composable
fun DomFileItem(
    modifier: Modifier = Modifier,
    documentFileUri: DocumentFileUri,
    onFileClicked: (file: FileUris) -> Unit
) {
    DomFileItem(
        modifier = modifier,
        uri = FileUris(
            fileUri = Uri.parse(documentFileUri.string),
            fileName = documentFileUri.fileName,
            fileSize = documentFileUri.size,
            mimeType = documentFileUri.mimeType,
            filePreviewUri = null
        ),
        onFileClicked = onFileClicked
    )
}

@Composable
fun DomFileItem(
    modifier: Modifier = Modifier,
    uri: FileUris,
    onFileClicked: (file: FileUris) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = DefaultHorizontalPadding)
            .clickable {
                onFileClicked(uri)
            }
    ) {
        Card(
            modifier = Modifier
                .size(100.dp)
        ) {
            if (uri.filePreviewUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(uri.filePreviewUri),
                    contentDescription = ""
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(uri.fileUri),
                    contentDescription = ""
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 12.dp)
        ) {
            Text(
                text = "Name: ${uri.fileName}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Size: ${uri.fileSize}"
            )
            Text(text = "Extension: ${uri.mimeType}")
        }
    }
}