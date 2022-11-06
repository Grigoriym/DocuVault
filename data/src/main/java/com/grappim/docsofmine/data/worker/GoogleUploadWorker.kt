package com.grappim.docsofmine.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.grappim.docsofmine.common.async.IoDispatcher
import com.grappim.docsofmine.data.gdrive.GoogleDriveManager
import com.grappim.domain.Try
import com.grappim.domain.repository.DocumentRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@HiltWorker
class GoogleUploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val documentRepository: DocumentRepository,
    private val googleDriveManager: GoogleDriveManager
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        val docs = documentRepository.getAllUnSynced()
        if (docs.isEmpty()) {
            return@withContext Result.success()
        }
        when (googleDriveManager.uploadFiles(docs)) {
            is Try.Success -> {
                documentRepository.markAsSynced()
                return@withContext Result.success()
            }
            is Try.Error -> {
                return@withContext Result.failure()
            }
        }
    }
}