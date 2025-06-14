package com.grappim.docuvault.feature.docs.repoimpl.di

import com.grappim.docuvault.feature.docs.repoapi.usecase.CancelEditDocumentChangesUseCase
import com.grappim.docuvault.feature.docs.repoapi.usecase.EditDocumentFinalizeUseCase
import com.grappim.docuvault.feature.docs.repoapi.usecase.EditDocumentPreparationUseCase
import com.grappim.docuvault.feature.docs.repoimpl.usecase.CancelEditEditDocumentChangesUseCaseImpl
import com.grappim.docuvault.feature.docs.repoimpl.usecase.EditDocumentFinalizeUseCaseImpl
import com.grappim.docuvault.feature.docs.repoimpl.usecase.EditDocumentPreparationUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
interface UseCaseBindsModule {
    @Binds
    fun bindEditDocumentPreparationUseCase(
        impl: EditDocumentPreparationUseCaseImpl
    ): EditDocumentPreparationUseCase

    @Binds
    fun bindEditDocumentFinalizeUseCase(
        impl: EditDocumentFinalizeUseCaseImpl
    ): EditDocumentFinalizeUseCase

    @Binds
    fun bindCancelDocumentChangesUseCase(
        impl: CancelEditEditDocumentChangesUseCaseImpl
    ): CancelEditDocumentChangesUseCase
}
