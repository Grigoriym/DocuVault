package com.grappim.docuvault.uikit.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.grappim.docuvault.feature.docs.uiapi.DocumentFileUI
import com.grappim.docuvault.uikit.theme.DefaultHorizontalPadding

@Composable
fun PlatoFileItem(
    modifier: Modifier = Modifier,
    fileData: DocumentFileUI,
    onFileClicked: (file: DocumentFileUI) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = DefaultHorizontalPadding)
            .clickable {
                onFileClicked(fileData)
            }
    ) {
        Card(
            modifier = Modifier
                .size(100.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            PlatoImage(painter = rememberAsyncImagePainter(fileData.uri))
        }

        Column(
            modifier = Modifier
                .padding(start = 12.dp)
        ) {
            Text(
                text = "Name: ${fileData.name}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Size: ${fileData.size}"
            )
            Text(text = "Extension: ${fileData.mimeType}")
        }
    }
}
