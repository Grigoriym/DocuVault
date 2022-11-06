package com.grappim.docsofmine.uikit.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.grappim.docsofmine.uikit.theme.DocsOfMineTheme

@Composable
fun DomTextFieldDefault(
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
private fun DomTextFieldDefaultPreview() {
    DocsOfMineTheme {
        DomTextFieldDefault(
            value = "asd",
            onValueChange = {},
            label = "name"
        )
    }
}