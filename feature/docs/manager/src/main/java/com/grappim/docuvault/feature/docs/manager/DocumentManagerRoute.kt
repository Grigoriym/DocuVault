package com.grappim.docuvault.feature.docs.manager

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grappim.docuvault.feature.docs.uiapi.DocumentFileUI
import com.grappim.docuvault.feature.group.uiapi.GroupUI
import com.grappim.docuvault.uikit.theme.DefaultArrangement
import com.grappim.docuvault.uikit.theme.DefaultHorizontalPadding
import com.grappim.docuvault.uikit.theme.DocuVaultTheme
import com.grappim.docuvault.uikit.widget.PlatoAlertDialog
import com.grappim.docuvault.uikit.widget.PlatoButtonDefault
import com.grappim.docuvault.uikit.widget.PlatoFileItem
import com.grappim.docuvault.uikit.widget.PlatoGroupItem
import com.grappim.docuvault.uikit.widget.PlatoLoadingDialog
import com.grappim.docuvault.uikit.widget.PlatoSnackbar
import com.grappim.docuvault.uikit.widget.PlatoTextFieldDefault
import com.grappim.docuvault.utils.files.MimeTypes
import com.grappim.docuvault.utils.files.models.CameraTakePictureData
import com.grappim.docuvault.utils.ui.LaunchedEffectResult
import com.grappim.docuvault.utils.ui.NativeText
import com.grappim.docuvault.utils.ui.asString

@Composable
fun DocumentManagerRoute(
    viewModel: DocumentManagerViewModel = hiltViewModel(),
    onDocumentDone: (isNewProduct: Boolean) -> Unit,
    goBack: (isNewProduct: Boolean) -> Unit
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val snackBarMessage by viewModel.snackBarMessage.collectAsState(
        initial = LaunchedEffectResult(
            data = NativeText.Empty,
            timestamp = 0L
        )
    )

    AddDocumentScreen(
        state = state,
        snackBarMessage = snackBarMessage,
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
     * Performs actions associated with quitting the screen.
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
    snackBarMessage: LaunchedEffectResult<out NativeText>,
    onDocumentDone: (isNewProduct: Boolean) -> Unit,
    goBack: (isNewProduct: Boolean) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(state.documentSaved) {
        if (state.documentSaved) {
            onDocumentDone(state.isNewDocument)
        }
    }
    BackHandler(enabled = true) {
        handleBackAction(state)
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

    DocumentManagerContent(
        state = state,
        snackBarMessage = snackBarMessage
    )
}

@Composable
private fun DocumentManagerContent(
    state: DocumentManagerState,
    snackBarMessage: LaunchedEffectResult<out NativeText>
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val scaffoldState: ScaffoldState = rememberScaffoldState()
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

    LaunchedEffect(snackBarMessage) {
        if (snackBarMessage.data !is NativeText.Empty) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = snackBarMessage.data.asString(context)
            )
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                PlatoSnackbar(
                    snackbarData = data
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Create new document",
                    modifier = Modifier
                        .padding(horizontal = DefaultHorizontalPadding)
                )

                PlatoTextFieldDefault(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .padding(horizontal = DefaultHorizontalPadding),
                    value = state.documentName,
                    onValueChange = { state.setName(it) },
                    label = "Name"
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
                        filesLauncher.launch(MimeTypes.mimeTypesForDocumentPicker)
                    }
                )
                FilesInfoContent(
                    filesUris = state.files
                )
                AddedFilesContent(
                    filesUris = state.files,
                    onFileRemoved = {
                        state.onFileRemoved(it)
                    }
                )
            }

            PlatoButtonDefault(
                onClick = {
                    state.onDocumentDone()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 12.dp,
                        bottom = 16.dp
                    )
                    .padding(horizontal = DefaultHorizontalPadding),
                text = state.bottomBarButtonText.asString(context)
            )
        }
    }
}

@Composable
private fun FilesInfoContent(filesUris: List<DocumentFileUI>) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .padding(horizontal = DefaultHorizontalPadding)
    ) {
        Text(text = "Files: ${filesUris.size}")
    }
}

@Composable
private fun GroupsContent(
    selectedGroup: GroupUI?,
    groups: List<GroupUI>,
    onGroupClick: (GroupUI) -> Unit
) {
    Text(
        text = "Select Group",
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = DefaultHorizontalPadding)
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
private fun AddedFilesContent(
    filesUris: List<DocumentFileUI>,
    onFileRemoved: (DocumentFileUI) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            top = 8.dp,
            bottom = 8.dp
        )
    ) {
        items(
            items = filesUris,
            key = { uris ->
                uris.uri
            }
        ) { uri ->
            val dismissState = rememberDismissState()
            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                onFileRemoved(uri)
            }
            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                background = {
                    val color by animateColorAsState(
                        if (dismissState.targetValue == DismissValue.Default) {
                            Color.White
                        } else {
                            Color.Red
                        },
                        label = ""
                    )
                    val scale by animateFloatAsState(
                        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f,
                        label = ""
                    )

                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        if (dismissState.targetValue == DismissValue.DismissedToStart) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "",
                                modifier = Modifier.scale(scale)
                            )
                        }
                    }
                }
            ) {
                PlatoFileItem(
                    fileData = uri,
                    onFileClicked = {}
                )
            }
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
        text = "Add from:",
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = DefaultHorizontalPadding)
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
            snackBarMessage = LaunchedEffectResult(NativeText.Simple("tests")),
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
                onShowAlertDialog = {}
            )
        )
    }
}
