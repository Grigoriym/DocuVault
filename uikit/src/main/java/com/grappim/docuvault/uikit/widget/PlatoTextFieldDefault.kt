package com.grappim.docuvault.uikit.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.grappim.docuvault.uikit.theme.DocuVaultTheme

@Composable
fun PlatoTextFieldDefault(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    label: String
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        singleLine = true,
        label = {
            Text(text = label)
        }
    )
}

@Composable
@Preview(
    showBackground = true
)
private fun PlatoTextFieldDefaultPreview() {
    DocuVaultTheme {
        PlatoTextFieldDefault(
            value = "asd",
            onValueChange = {},
            label = "name"
        )
    }
}
