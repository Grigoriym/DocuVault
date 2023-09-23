package com.grappim.docsofmine.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.grappim.docsofmine.common.async.IoDispatcher
import com.grappim.docsofmine.data.gdrive.GoogleDriveManager
import com.grappim.domain.model.document.Document
import com.grappim.domain.model.document.DocumentFileData
import com.grappim.domain.repository.DocumentRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@HiltWorker
class GoogleDownloadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val documentRepository: DocumentRepository,
    private val googleDriveManager: GoogleDriveManager
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        val localDocs = documentRepository.getAllDocuments()
        val files = googleDriveManager.downloadFiles(localDocs)
        if (files.isNotEmpty()) {
            val docs = files.map { localGDriveParentFile ->
                val filesUri = localGDriveParentFile.children.map { child ->
                    DocumentFileData(
                        name = child.name,
                        mimeType = child.mimeType,
                        uriPath = child.fileUri.path!!,
                        uriString = child.fileUri.toString(),
                        size = child.size,
                        previewUriString = child.filePreviewUriString,
                        previewUriPath = child.filePreviewUriPath,
                        md5 = child.md5
                    )
                }

                Document(
                    id = localGDriveParentFile.id,
                    name = localGDriveParentFile.name,
                    group = localGDriveParentFile.group,
                    filesUri = filesUri,
                    createdDate = localGDriveParentFile.createdTime!!,
                    isSynced = true
                )
            }
            documentRepository.addDocuments(docs)
        }
        Result.success()
    }
}