package com.grappim.docsofmine.navigation

sealed interface DocumentsNavDestinations {
    val route: String

    object Details : DocumentsNavDestinations {

        private const val pathPrefix = "documents_details_destination/"

        override val route: String =
            "$pathPrefix{${documentIdArgument()}}"

        fun documentIdArgument(): String = "documentIdArgument"

        fun createRoute(documentId: String) = "$pathPrefix$documentId"

    }
}