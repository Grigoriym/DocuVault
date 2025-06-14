package com.grappim.docuvault.feature.docs.repoapi.usecase

data class CancelDocumentChangesData(
    val documentId: Long,
    val documentFolderName: String
)
