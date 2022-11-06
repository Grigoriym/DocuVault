package com.grappim.docsofmine.ui.screens.main.settings.drive

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.grappim.docsofmine.data.gdrive.AuthResultContract
import com.grappim.docsofmine.uikit.theme.DefaultHorizontalPadding
import timber.log.Timber

@Composable
fun GoogleDriveScreen(
    viewModel: GoogleDriveViewModel = hiltViewModel()
) {
    val signInRequestCode = 1
    val startForResult =
        rememberLauncherForActivityResult(AuthResultContract()) { task ->
            task?.addOnSuccessListener {
                viewModel.updateAccountInfo(it)
                Timber.d("google sign in success")
            }?.addOnFailureListener {
                Timber.e(it)
            }
        }
    val uiState by viewModel.uiState.collectAsState()

    GoogleDriveScreenContent(
        uiState = uiState,
        clickOnLogIn = {
            startForResult.launch(signInRequestCode)
        },
        clickOnLogOut = {
            viewModel.logOut()
        },
        clickOnUpload = {
            viewModel.upload()
        },
        clickOnDownload = {
            viewModel.download()
        },
        clickOnAccessFiles ={
            viewModel.accessFiles()
        }
    )
}

@Composable
private fun GoogleDriveScreenContent(
    uiState: GoogleAuthState,
    clickOnLogIn: () -> Unit,
    clickOnLogOut: () -> Unit,
    clickOnUpload: () -> Unit,
    clickOnDownload: () -> Unit,
    clickOnAccessFiles:() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DefaultHorizontalPadding)
    ) {
        if (uiState.isUserSignIn.not()) {
            Button(onClick = clickOnLogIn) {
                Text(text = "login")
            }
        } else {
            Text(text = uiState.currentUser?.email ?: "")
            Button(onClick = clickOnLogOut) {
                Text(text = "Logout")
            }
            Button(onClick = clickOnUpload) {
                Text(text = "Upload")
            }
            Button(onClick = clickOnDownload) {
                Text(text = "Download")
            }
            Button(onClick = clickOnAccessFiles) {
                Text(text = "accessFiles")
            }
        }
    }
}