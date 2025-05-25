package com.grappim.docuvault.uikit.widget

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grappim.docuvault.uikit.theme.DocuVaultTheme

@Composable
fun PlatoButtonDefault(modifier: Modifier = Modifier, onClick: () -> Unit, text: String) {
    Button(
        modifier = modifier
            .height(42.dp),
        onClick = onClick
    ) {
        Text(text = text)
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun DomButtonDefaultPreview() {
    DocuVaultTheme {
        PlatoButtonDefault(
            onClick = {},
            text = "Text"
        )
    }
}
