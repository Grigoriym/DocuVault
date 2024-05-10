package com.grappim.docuvault.feature.docmanager.ui

import android.net.Uri
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
import com.grappim.docuvault.feature.group.domain.Group
import com.grappim.docuvault.uikit.DocumentFileUiData
import com.grappim.docuvault.uikit.R
import com.grappim.docuvault.uikit.theme.DefaultArrangement
import com.grappim.docuvault.uikit.theme.DefaultHorizontalPadding
import com.grappim.docuvault.uikit.theme.DocuVaultTheme
import com.grappim.docuvault.uikit.widget.PlatoAlertDialog
import com.grappim.docuvault.uikit.widget.PlatoButtonDefault
import com.grappim.docuvault.uikit.widget.PlatoFileItem
import com.grappim.docuvault.uikit.widget.PlatoGroupItem
import com.grappim.docuvault.uikit.widget.PlatoSnackbar
import com.grappim.docuvault.uikit.widget.PlatoTextFieldDefault
import com.grappim.docuvault.utils.files.models.CameraTakePictureData
import com.grappim.docuvault.utils.ui.LaunchedEffectResult
import com.grappim.docuvault.utils.ui.NativeText
import com.grappim.docuvault.utils.ui.asString
import com.grappim.domain.model.MimeTypes

@Composable
fun DocumentManagerRoute(
    viewModel: DocumentManagerViewModel = hiltViewModel(),
    onDocumentDone: () -> Unit,
    goBack: () -> Unit
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

@Composable
private fun AddDocumentScreen(
    state: DocumentManagerState,
    snackBarMessage: LaunchedEffectResult<out NativeText>,
    onDocumentDone: () -> Unit,
    goBack: () -> Unit
) {
    LaunchedEffect(state.documentSaved) {
        if (state.documentSaved) {
            onDocumentDone()
        }
    }
//    BackHandler(enabled = true) {
//        handleBackAction(state)
//    }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var backEnabled by remember {
        mutableStateOf(true)
    }
    var showAlertDialog by remember {
        mutableStateOf(false)
    }

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

    PlatoAlertDialog(
        text = stringResource(id = R.string.save_changes_before_exit),
        showAlertDialog = showAlertDialog,
        onDismissRequest = {
            showAlertDialog = false
            backEnabled = false
//            viewModel.removeData()
            goBack()
        },
        onConfirmButtonClicked = {
            showAlertDialog = false
            backEnabled = false
//            viewModel.saveData()
        }
    ) {
    }

    AddDocumentScreenContent(
        documentName = state.documentName,
        setDocumentName = {
            state.setName(it)
        },
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
        groups = state.groups,
        onCreateClick = {
            state.onDocumentDone()
        },
        filesUris = state.files,
        onFilesClick = {
            filesLauncher.launch(MimeTypes.mimeTypesForDocumentPicker)
        },
        onGroupClick = { group: Group ->
            state.onGroupSelected(group)
        },
        selectedGroup = state.selectedGroup,
        scaffoldState = scaffoldState,
        onFileRemoved = {
            state.onFileRemoved(it)
        }
    )
}

@Composable
private fun AddDocumentScreenContent(
    documentName: String,
    setDocumentName: (String) -> Unit,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    groups: List<Group>,
    onCreateClick: () -> Unit,
    filesUris: List<DocumentFileUiData>,
    onFilesClick: () -> Unit,
    onGroupClick: (Group) -> Unit,
    selectedGroup: Group?,
    scaffoldState: ScaffoldState,
    onFileRemoved: (DocumentFileUiData) -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                PlatoSnackbar(
                    snackbarData = data
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
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
                    value = documentName,
                    onValueChange = setDocumentName,
                    label = "Name"
                )
                GroupsContent(
                    groups = groups,
                    onGroupClick = onGroupClick,
                    selectedGroup = selectedGroup
                )

                AddFromContent(
                    onGalleryClick = onGalleryClick,
                    onCameraClick = onCameraClick,
                    onFilesClick = onFilesClick
                )
                FilesInfoContent(
                    filesUris = filesUris
                )
                AddedFilesContent(
                    filesUris = filesUris,
                    onFileRemoved = onFileRemoved
                )
            }

            PlatoButtonDefault(
                onClick = onCreateClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 12.dp,
                        bottom = 16.dp
                    )
                    .padding(horizontal = DefaultHorizontalPadding),
                text = "Create"
            )
        }
    }
}

@Composable
private fun FilesInfoContent(filesUris: List<DocumentFileUiData>) {
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
    groups: List<Group>,
    onGroupClick: (Group) -> Unit,
    selectedGroup: Group?
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
                group = group,
                onGroupClick = onGroupClick,
                isSelected = group.id == selectedGroup?.id
            )
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun AddedFilesContent(
    filesUris: List<DocumentFileUiData>,
    onFileRemoved: (DocumentFileUiData) -> Unit
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
        AddDocumentScreenContent(
            documentName = "",
            setDocumentName = {},
            onGalleryClick = {},
            onCameraClick = {},
            groups = listOf(
                Group(
                    id = 9587,
                    name = "Renee Riggs",
                    fields = listOf(),
                    color = "veri"
                ),
                Group(
                    id = 12,
                    name = "Renee Riggs",
                    fields = listOf(),
                    color = "veri"
                )
            ),
            onCreateClick = {},
            filesUris = emptyList(),
            onFilesClick = {},
            onGroupClick = {},
            selectedGroup = Group(
                id = 8833,
                name = "Cindy Whitfield",
                fields = listOf(),
                color = "tempor"
            ),
            scaffoldState = rememberScaffoldState(),
            onFileRemoved = {}
        )
    }
}
