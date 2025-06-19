package com.grappim.docuvault.feature.docs.manager

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grappim.docuvault.feature.docgroup.uiapi.GroupUI
import com.grappim.docuvault.feature.docs.uiapi.DocumentFileUI
import com.grappim.docuvault.uikit.theme.DefaultArrangement
import com.grappim.docuvault.uikit.theme.DefaultHorizontalPadding
import com.grappim.docuvault.uikit.theme.DocuVaultTheme
import com.grappim.docuvault.uikit.widget.PlatoAlertDialog
import com.grappim.docuvault.uikit.widget.PlatoButtonDefault
import com.grappim.docuvault.uikit.widget.PlatoFileItem
import com.grappim.docuvault.uikit.widget.PlatoGroupItem
import com.grappim.docuvault.uikit.widget.PlatoLoadingDialog
import com.grappim.docuvault.uikit.widget.PlatoTextFieldDefault
import com.grappim.docuvault.utils.filesapi.mimeTypesForDocumentPicker
import com.grappim.docuvault.utils.filesapi.models.CameraTakePictureData
import com.grappim.docuvault.utils.ui.NativeText
import com.grappim.docuvault.utils.ui.asString
import com.grappim.docuvault.utils.ui.collectSnackbarMessage

@Composable
fun DocumentManagerRoute(
    viewModel: DocumentManagerViewModel = hiltViewModel(),
    onDocumentDone: (isNewDoc: Boolean) -> Unit,
    goBack: (isNewDoc: Boolean) -> Unit,
    onShowSnackbar: suspend (message: String, String?) -> Boolean
) {
    val context = LocalContext.current

    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val snackBarMessage by viewModel.snackBarMessage.collectSnackbarMessage()

    BackHandler(enabled = true) {
        handleBackAction(state)
    }

    LaunchedEffect(snackBarMessage) {
        if (snackBarMessage !is NativeText.Empty) {
            onShowSnackbar(snackBarMessage.asString(context), null)
        }
    }

    AddDocumentScreen(
        state = state,
        onDocumentDone = onDocumentDone,
        goBack = goBack
    )
}

/**
 * Handles the back action for the HateOrRateScreen.
 *
 * This function determines the appropriate action when the back button is pressed
 * or when a back navigation event is triggered. It uses the current state of the
 * screen to decide whether to show a confirmation dialog, immediately quit the screen,
 * or perform other actions.
 */
fun handleBackAction(state: DocumentManagerState) {
    /**
     * Performs actions associated when quitting the screen.
     *
     * This inner function encapsulates the logic for quitting the screen,
     * including hiding the alert dialog and invoking the onQuit callback
     * from the screen's state. This function is called when it's determined
     * that the user can safely exit the screen without additional confirmation.
     */
    fun doOnQuit() {
        state.onShowAlertDialog(false)
        state.onQuit()
    }

    if (state.forceQuit) {
        doOnQuit()
        return
    }

    if (state.files.isNotEmpty() || state.documentName.isNotEmpty()) {
        // If there are unsaved changes (indicated by non-empty product name or images),
        // prompt the user with an alert dialog to confirm their intent to quit.
        state.onShowAlertDialog(true)
    } else {
        doOnQuit()
    }
}

@Composable
private fun AddDocumentScreen(
    state: DocumentManagerState,
    onDocumentDone: (isNewProduct: Boolean) -> Unit,
    goBack: (isNewProduct: Boolean) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(state.documentSaved) {
        if (state.documentSaved) {
            onDocumentDone(state.isNewDocument)
        }
    }

    LaunchedEffect(state.forceQuit) {
        if (state.forceQuit) {
            handleBackAction(state)
        }
    }

    LaunchedEffect(state.quitStatus) {
        if (state.quitStatus is QuitStatus.Finish) {
            goBack(state.isNewDocument)
        }
    }

    PlatoLoadingDialog(isLoading = state.quitStatus is QuitStatus.InProgress)

    PlatoAlertDialog(
        text = state.alertDialogText.asString(context),
        showAlertDialog = state.showAlertDialog,
        confirmButtonText = stringResource(id = R.string.ok),
        onDismissRequest = {
            state.onShowAlertDialog(false)
        },
        onConfirmButtonClicked = {
            state.onForceQuit()
        },
        onDismissButtonClicked = {
            state.onShowAlertDialog(false)
        }
    )

    DocumentManagerContent(state = state)
}

@Composable
private fun DocumentManagerContent(state: DocumentManagerState) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var cameraTakePictureData by remember {
        mutableStateOf(CameraTakePictureData.empty())
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        keyboardController?.hide()
        uri?.let { state.onAddImageFromGalleryClicked(uri) }
    }
    val filesLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenMultipleDocuments()
    ) { uris: List<Uri> ->
        keyboardController?.hide()
        state.onMultipleFilesAdded(uris)
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess: Boolean ->
        keyboardController?.hide()
        if (isSuccess) {
            state.onAddCameraPictureClicked(cameraTakePictureData)
        }
    }

    Column {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            PlatoTextFieldDefault(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = DefaultHorizontalPadding),
                value = state.documentName,
                onValueChange = { state.setName(it) },
                label = stringResource(R.string.name_label)
            )
            PlatoTextFieldDefault(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = DefaultHorizontalPadding),
                value = state.documentDescription,
                onValueChange = { state.setDescription(it) },
                label = stringResource(R.string.description_label)
            )
            GroupsContent(
                selectedGroup = state.selectedGroup,
                groups = state.groups,
                onGroupClick = { group ->
                    state.onGroupSelected(group)
                }
            )

            AddFromContent(
                onGalleryClick = {
                    galleryLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                onCameraClick = {
                    cameraTakePictureData = state.getCameraImageFileUri()
                    cameraLauncher.launch(cameraTakePictureData.uri)
                },
                onFilesClick = {
                    filesLauncher.launch(mimeTypesForDocumentPicker)
                }
            )
            FilesInfoContent(
                filesUris = state.files
            )
            AddedFilesContent(
                state = state
            )
        }

        PlatoButtonDefault(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 12.dp,
                    bottom = 16.dp
                )
                .padding(horizontal = DefaultHorizontalPadding),
            onClick = {
                state.onDocumentDone()
            },
            text = state.bottomBarButtonText.asString(context)
        )
    }
}

@Composable
private fun FilesInfoContent(filesUris: List<DocumentFileUI>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = DefaultHorizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.files_size, filesUris.size),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun GroupsContent(
    selectedGroup: GroupUI?,
    groups: List<GroupUI>,
    onGroupClick: (GroupUI) -> Unit
) {
    Text(
        text = stringResource(R.string.select_group),
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = DefaultHorizontalPadding),
        style = MaterialTheme.typography.titleLarge
    )
    LazyRow(
        modifier = Modifier
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(DefaultArrangement),
        contentPadding = PaddingValues(start = 8.dp)
    ) {
        items(groups) { group ->
            PlatoGroupItem(
                color = group.color,
                id = group.id,
                name = group.name,
                isSelected = group.id == selectedGroup?.id,
                onGroupClick = {
                    onGroupClick(group)
                }
            )
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun AddedFilesContent(modifier: Modifier = Modifier, state: DocumentManagerState) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(DefaultArrangement),
        contentPadding = PaddingValues(start = 8.dp)
    ) {
        items(
            items = state.files,
            key = { uris ->
                uris.uri
            }
        ) { file ->
            PlatoFileItem(
                modifier = Modifier.size(120.dp),
                fileData = file,
                onFileClicked = {},
                isEditable = true
            )
        }
    }
}

@Composable
private fun AddFromContent(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onFilesClick: () -> Unit
) {
    Text(
        text = stringResource(R.string.add_from),
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = DefaultHorizontalPadding),
        style = MaterialTheme.typography.titleLarge
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DefaultHorizontalPadding),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onCameraClick
        ) {
            Text(text = stringResource(id = R.string.camera))
        }
        Button(
            onClick = onGalleryClick
        ) {
            Text(text = stringResource(id = R.string.gallery))
        }
        Button(
            onClick = onFilesClick
        ) {
            Text(text = stringResource(id = R.string.files))
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun AddDocumentScreenContentPreview() {
    DocuVaultTheme {
        DocumentManagerContent(
            state = DocumentManagerState(
                documentName = "Dudley Powell",
                isNewDocument = false,
                documentSaved = false,
                files = listOf(),
                selectedGroup = null,
                groups = listOf(),
                draftDocument = null,
                editDocument = null,
                quitStatus = QuitStatus.Initial,
                forceQuit = false,
                onGroupSelected = {},
                setName = {},
                onAddImageFromGalleryClicked = {},
                onAddCameraPictureClicked = {},
                getCameraImageFileUri = {
                    CameraTakePictureData.empty()
                },
                onFileRemoved = {},
                onMultipleFilesAdded = {},
                onDocumentDone = {},
                onQuit = {},
                onForceQuit = {},
                showAlertDialog = false,
                onShowAlertDialog = {},
                setDescription = {}
            )
        )
    }
}
