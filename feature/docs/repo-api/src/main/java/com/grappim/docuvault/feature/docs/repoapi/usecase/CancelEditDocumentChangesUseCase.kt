package com.grappim.docuvault.feature.docs.repoapi.usecase

interface CancelEditDocumentChangesUseCase {
    suspend fun cancel(data: CancelDocumentChangesData)
}
