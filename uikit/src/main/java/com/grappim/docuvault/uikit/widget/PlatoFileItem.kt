package com.grappim.docuvault.uikit.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.grappim.docuvault.feature.docs.uiapi.DocumentFileUI
import com.grappim.docuvault.uikit.theme.DocuVaultTheme
import com.grappim.docuvault.uikit.utils.ThemePreviews

/**
 * Widget that represents the document file item
 */
@Composable
fun PlatoFileItem(
    modifier: Modifier = Modifier,
    fileData: DocumentFileUI,
    onFileClicked: (file: DocumentFileUI) -> Unit,
    isEditable: Boolean = false
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        onClick = {
            onFileClicked(fileData)
        }
    ) {
        Box {
            PlatoImage(
                modifier = Modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(fileData.uri),
                contentScale = ContentScale.FillBounds
            )
            if (isEditable) {
                PlatoIconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd),
                    icon = Icons.Filled.MoreVert,
                    onButtonClick = {}
                )
            }
        }
    }
}

@Composable
@ThemePreviews
private fun PlatoFileItemPreview() {
    DocuVaultTheme {
        PlatoFileItem(
            fileData = DocumentFileUI(
                fileId = 4526,
                uri = "asdas".toUri(),
                name = "Harlan Cline",
                size = 6622,
                mimeType = "instructior",
                md5 = "maluisset",
                isEdit = false
            ),
            onFileClicked = {},
            isEditable = false
        )
    }
}

@Composable
@ThemePreviews
private fun PlatoFileItemEditablePreview() {
    DocuVaultTheme {
        PlatoFileItem(
            fileData = DocumentFileUI(
                fileId = 4526,
                uri = "asdas".toUri(),
                name = "Harlan Cline",
                size = 6622,
                mimeType = "instructior",
                md5 = "maluisset",
                isEdit = true
            ),
            onFileClicked = {},
            isEditable = true
        )
    }
}
