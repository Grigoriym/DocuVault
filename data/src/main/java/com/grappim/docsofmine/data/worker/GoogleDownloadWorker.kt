package com.grappim.docsofmine.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.grappim.docsofmine.data.gdrive.GoogleDriveManager
import com.grappim.docsofmine.common.async.IoDispatcher
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
        googleDriveManager.downloadFiles(localDocs)
        Result.success()
    }
}