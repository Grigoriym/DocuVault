package com.grappim.docsofmine.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkerHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object{
        private const val UNIQUE_GOOGLE_SYNC_WORK = "unique.google.sync.work"
        private const val UNIQUE_GOOGLE_DOWNLOAD_WORK = "unique.google.download.work"
    }

    fun startDriveDownloading() {
        val googleDownloadWorker = buildWorkRequest<GoogleDownloadWorker>()
        WorkManager.getInstance(context)
            .beginUniqueWork(
                UNIQUE_GOOGLE_DOWNLOAD_WORK,
                ExistingWorkPolicy.KEEP,
                googleDownloadWorker
            ).enqueue()
    }

    fun startDriveUploading() {
        val googleUploadWorker = buildWorkRequest<GoogleUploadWorker>()
        WorkManager.getInstance(context)
            .beginUniqueWork(
                UNIQUE_GOOGLE_SYNC_WORK,
                ExistingWorkPolicy.KEEP,
                googleUploadWorker
            ).enqueue()
    }

    private inline fun <reified T : ListenableWorker> buildWorkRequest(): OneTimeWorkRequest =
        OneTimeWorkRequestBuilder<T>()
            .setConstraints(
                Constraints
                    .Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            ).build()
}