package com.grappim.docuvault.feature.docmanager.ui

import android.net.Uri
import com.grappim.docuvault.uikit.DocumentFileUiData
import com.grappim.docuvault.utils.files.models.CameraTakePictureData
import com.grappim.domain.model.document.Document
import com.grappim.domain.model.document.DraftDocument
import com.grappim.domain.model.group.Group

data class DocumentManagerState(
    val documentName: String = "",
    val setName: (name: String) -> Unit,

    val isNewDocument: Boolean = true,

    val documentSaved: Boolean = false,

    val selectedGroup: Group? = null,
    val onGroupSelected: (group: Group) -> Unit,

    val draftDocument: DraftDocument? = null,
    val editDocument: Document? = null,

    val groups: List<Group> = emptyList(),

    val files: List<DocumentFileUiData> = emptyList(),

    val onAddImageFromGalleryClicked: (uri: Uri) -> Unit,
    val onAddCameraPictureClicked: (cameraTakePictureData: CameraTakePictureData) -> Unit,

    val getCameraImageFileUri: () -> CameraTakePictureData,
    val onFileRemoved: (DocumentFileUiData) -> Unit,
    val onMultipleFilesAdded: (List<Uri>) -> Unit,

    val onDocumentDone: () -> Unit
)
