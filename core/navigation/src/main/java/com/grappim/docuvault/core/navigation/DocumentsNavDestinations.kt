package com.grappim.docuvault.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface DocumentsNavDestinations {
    val route: String

    data object Details : DocumentsNavDestinations {

        private const val PREFIX = "documents_details_destination"

        const val KEY_DOC_ID = "keyDocId"

        override val route: String =
            "$PREFIX/?$KEY_DOC_ID={$KEY_DOC_ID}"

        const val IS_FROM_EDIT = "is_from_edit"

        fun createRoute(documentId: Long) = "$PREFIX/?$KEY_DOC_ID=$documentId"
    }

    data object DocManager : DocumentsNavDestinations {
        private const val PREFIX = "doc_manager"
        const val KEY_EDIT_DOC_ID = "keyEditDocId"

        override val route: String = "$PREFIX/?$KEY_EDIT_DOC_ID={$KEY_EDIT_DOC_ID}"

        val icon: ImageVector = Icons.Filled.Add

        fun getRouteToNavigate(id: String?) = "$PREFIX/?$KEY_EDIT_DOC_ID=$id"
    }
}
