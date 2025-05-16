package com.grappim.docuvault.ui.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.grappim.docuvault.uikit.theme.DocuVaultTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DocuVaultActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            DocuVaultTheme {
                MainScreen()
            }
        }
    }
}
