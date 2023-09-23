package com.grappim.domain.drive

import com.grappim.domain.Try
import com.grappim.domain.model.document.Document

interface DriveManager {

    suspend fun uploadFiles(
        localDocs: List<Document>
    ): Try<List<Document>, Throwable>


}