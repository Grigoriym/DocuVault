package com.grappim.docuvault.feature.docgroup.details

import com.grappim.docuvault.feature.docgroup.repoapi.model.Group
import com.grappim.docuvault.feature.docs.uiapi.DocumentListUI

data class GroupDetailsState(
    val group: Group? = null,
    val documents: List<DocumentListUI> = emptyList(),
    val groupDeleted: Boolean = false,
    val onDeleteGroupConfirm: () -> Unit,
    val showDeletionDialog: Boolean = false,
    val onShowAlertDialog: (show: Boolean) -> Unit,
    val onDeleteClicked: () -> Unit,
    /**
     * We call this when we go back from the manager screen
     * to update the current group to show the updated values
     */
    val updateGroup: () -> Unit
)
