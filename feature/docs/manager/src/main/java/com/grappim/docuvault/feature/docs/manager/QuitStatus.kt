package com.grappim.docuvault.feature.docs.manager

sealed interface QuitStatus {
    data object Initial : QuitStatus
    data object InProgress : QuitStatus
    data object Finish : QuitStatus
}
