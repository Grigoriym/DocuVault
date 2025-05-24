package com.grappim.docuvault.uikit.widget

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.grappim.docuvault.uikit.R
import com.grappim.docuvault.uikit.theme.DocuVaultTheme
import com.grappim.docuvault.uikit.utils.ThemePreviews

const val PLATO_ALERT_DIALOG_TAG = "plato_alert_dialog_tag"

@Composable
fun PlatoAlertDialog(
    modifier: Modifier = Modifier,
    text: String,
    showAlertDialog: Boolean,
    confirmButtonText: String = stringResource(id = R.string.yes),
    dismissButtonText: String = stringResource(id = R.string.cancel),
    onDismissRequest: () -> Unit,
    onConfirmButtonClicked: () -> Unit,
    onDismissButtonClicked: () -> Unit
) {
    if (showAlertDialog) {
        AlertDialog(
            modifier = modifier
                .testTag(PLATO_ALERT_DIALOG_TAG),
            shape = MaterialTheme.shapes.medium.copy(
                all = CornerSize(16.dp)
            ),
            onDismissRequest = onDismissRequest,
            title = {
                Text(text = text)
            },
            confirmButton = {
                Button(
                    onClick = onConfirmButtonClicked
                ) {
                    Text(confirmButtonText)
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismissButtonClicked
                ) {
                    Text(dismissButtonText)
                }
            }
        )
    }
}

@[Composable ThemePreviews]
private fun PlatoAlertDialogPreview() {
    DocuVaultTheme {
        PlatoAlertDialog(
            text = "Some text",
            showAlertDialog = true,
            onDismissRequest = {},
            onConfirmButtonClicked = {},
            onDismissButtonClicked = {}
        )
    }
}
