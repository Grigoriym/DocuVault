package com.grappim.docuvault.feature.docs.manager

import android.net.Uri
import com.grappim.docuvault.feature.docs.domain.Document
import com.grappim.docuvault.feature.docs.domain.DraftDocument
import com.grappim.docuvault.feature.docs.uiapi.DocumentFileUI
import com.grappim.docuvault.feature.group.uiapi.GroupUI
import com.grappim.docuvault.utils.files.models.CameraTakePictureData
import com.grappim.docuvault.utils.ui.NativeText

data class DocumentManagerState(
    val documentName: String = "",
    val isNewDocument: Boolean = true,
    val documentSaved: Boolean = false,
    val files: List<DocumentFileUI> = emptyList(),

    val selectedGroup: GroupUI? = null,
    val groups: List<GroupUI> = emptyList(),

    val draftDocument: DraftDocument? = null,
    val editDocument: Document? = null,

    val quitStatus: QuitStatus = QuitStatus.Initial,
    val forceQuit: Boolean = false,

    val onGroupSelected: (group: GroupUI) -> Unit,
    val setName: (name: String) -> Unit,

    val onAddImageFromGalleryClicked: (uri: Uri) -> Unit,
    val onAddCameraPictureClicked: (cameraTakePictureData: CameraTakePictureData) -> Unit,

    val getCameraImageFileUri: () -> CameraTakePictureData,
    val onFileRemoved: (DocumentFileUI) -> Unit,
    val onMultipleFilesAdded: (List<Uri>) -> Unit,

    val onDocumentDone: () -> Unit,
    val onQuit: () -> Unit,
    val onForceQuit: () -> Unit,

    val showAlertDialog: Boolean = false,
    val onShowAlertDialog: (show: Boolean) -> Unit,

    val bottomBarButtonText: NativeText = NativeText.Empty,
    val alertDialogText: NativeText = NativeText.Empty
)
