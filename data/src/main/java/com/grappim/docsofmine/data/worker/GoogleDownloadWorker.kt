package com.grappim.docsofmine.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.grappim.docsofmine.common.async.IoDispatcher
import com.grappim.docsofmine.data.gdrive.GoogleDriveManager
import com.grappim.docsofmine.data.mappers.toDomain
import com.grappim.docsofmine.data.model.document.DocumentDTO
import com.grappim.docsofmine.utils.files.FileUtils
import com.grappim.domain.model.document.Document
import com.grappim.domain.model.document.DocumentFileData
import com.grappim.domain.repository.DocumentRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

@HiltWorker
class GoogleDownloadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val documentRepository: DocumentRepository,
    private val googleDriveManager: GoogleDriveManager,
    private val json: Json,
    private val fileUtils: FileUtils
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        val localDocs = documentRepository.getAllDocuments()
        val files = googleDriveManager.downloadFiles(localDocs)
        if (files.isNotEmpty()) {
            val docs = files.map {
                val fileId = it.appProperties!![GoogleDriveManager.JSON_APP_ID]!!.toLong()
                val fileDate = it.appProperties!![GoogleDriveManager.JSON_APP_DATE]!!
                val folderName = "${fileId}_$fileDate"
                val jsonFile = File(
                    fileUtils.getFolder(folderName),
                    "$folderName.json"
                )
                val jsonString = jsonFile.readText()
                val decodedJsonDTO = json.decodeFromString<DocumentDTO>(jsonString)
                val domain = decodedJsonDTO.toDomain()

                val filesUri = it.files.map { fileUri ->
                    DocumentFileData(
                        name = fileUri.name,
                        mimeType = fileUri.mimeType!!,
                        uriPath = fileUri.fileUri!!.path!!,
                        uriString = fileUri.fileUri!!.toString(),
                        size = fileUri.size,
                        previewUriString = fileUri.filePreviewUriString,
                        previewUriPath = fileUri.filePreviewUriPath
                    )
                }

                Document(
                    id = domain.id,
                    name = domain.name,
                    group = domain.group,
                    filesUri = filesUri,
                    createdDate = it.createdTime!!
                )
            }

            documentRepository.addDocuments(docs)
        }
        Result.success()
    }
}