package com.grappim.domain.drive

sealed interface UploadFilesResult {

    object Success : UploadFilesResult
    data class Error(val throwable: Throwable) : UploadFilesResult
}