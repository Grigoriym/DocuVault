package com.grappim.docuvault.core.navigation

sealed interface DocumentsNavDestinations {
    val route: String

    data object Details : DocumentsNavDestinations {

        private const val PATH_PREFIX = "documents_details_destination/"

        override val route: String =
            "$PATH_PREFIX{${documentIdArgument()}}"

        fun documentIdArgument(): String = "documentIdArgument"

        fun createRoute(documentId: String) = "$PATH_PREFIX$documentId"
    }
}
