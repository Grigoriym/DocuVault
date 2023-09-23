package com.grappim.docsofmine.data.gdrive

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.drive.DriveFolder
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.grappim.docsofmine.common.async.IoDispatcher
import com.grappim.docsofmine.data.BuildConfig
import com.grappim.docsofmine.data.mappers.toUploadDTO
import com.grappim.docsofmine.data.model.document.DocumentUploadDTO
import com.grappim.docsofmine.data.runOperationCatching
import com.grappim.docsofmine.gdrive.GDriveFileMapper
import com.grappim.docsofmine.gdrive.GDriveFileWrapper
import com.grappim.docsofmine.gdrive.LocalGDriveChildFile
import com.grappim.docsofmine.gdrive.LocalGDriveParentFile
import com.grappim.docsofmine.uikit.R
import com.grappim.docsofmine.utils.dateTime.DateTimeUtils
import com.grappim.docsofmine.utils.files.FileUtils
import com.grappim.docsofmine.utils.files.HashUtils
import com.grappim.docsofmine.utils.files.mime.MimeTypes
import com.grappim.domain.Try
import com.grappim.domain.drive.DriveManager
import com.grappim.domain.model.document.Document
import com.grappim.domain.model.document.DocumentFileData
import com.grappim.domain.model.group.Group
import com.grappim.domain.storage.GoogleDrivePrefs
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleDriveManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val googleDrivePrefs: GoogleDrivePrefs,
    private val fileUtils: FileUtils,
    private val dateTimeUtils: DateTimeUtils,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val json: Json,
    private val hashUtils: HashUtils,
    private val gDriveFileMapper: GDriveFileMapper
) : DriveManager {

    /**
     * https://developers.google.com/drive/api/v3/reference/files
     */
    companion object {
        private const val DEFAULT_FIELDS =
            "nextPageToken, files(id,name,size,createdTime,modifiedTime," +
                    "starred,mimeType,appProperties,parents)"

        const val JSON_APP_PROPERTY_NAME = "json.app.property.name"
        const val JSON_APP_ID = "json.app.id"
        const val JSON_APP_DATE = "json.app.date"
    }

    fun getLastSignedInAccount(): GoogleSignInAccount? =
        GoogleSignIn.getLastSignedInAccount(context)

    fun getGoogleSignInClient(): GoogleSignInClient {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.SERVER_CLIENT_ID)
            .requestEmail()
            .requestProfile()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE), Scope(DriveScopes.DRIVE_APPDATA))
            .build()

        return GoogleSignIn.getClient(context, signInOptions)
    }

    private fun getDriveService(): Drive? {
        GoogleSignIn.getLastSignedInAccount(context)?.let { googleAccount ->
            val credential = GoogleAccountCredential.usingOAuth2(
                context,
                listOf(DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_APPDATA)
            )
            credential.selectedAccount = googleAccount.account!!
            return Drive.Builder(
                NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                credential
            ).setApplicationName(context.getString(R.string.app_name))
                .build()
        }
        Timber.d("drive service is null")
        return null
    }

    private suspend fun getRootFolderId(): String? {
        var folderId = googleDrivePrefs.googleDriveRootFolder
        if (folderId == null) {
            folderId = searchFile(
                fileName = context.getString(R.string.app_name),
                mimeType = DriveFolder.MIME_TYPE
            ).firstOrNull()?.gDriveFileId
            if (folderId == null) {
                folderId = createFolder(
                    name = context.getString(R.string.app_name),
                    isRoot = true
                )
            }
        }
        return folderId
    }

    private suspend fun getDocumentFolderId(
        document: Document
    ): String? {
        val documentFolderName = fileUtils.getDocumentFolderName(document)
        var folderId = searchFile(
            fileName = documentFolderName,
            mimeType = DriveFolder.MIME_TYPE
        ).firstOrNull()?.gDriveFileId
        if (folderId == null) {
            folderId = createFolder(documentFolderName)
        }
        return folderId
    }

    private suspend fun searchDocumentFolder(
        document: Document
    ): List<GDriveFileWrapper> {
        val documentFolderName = fileUtils.getDocumentFolderName(document)
        return searchFile(
            fileName = documentFolderName,
            mimeType = DriveFolder.MIME_TYPE
        )
    }

    private suspend fun searchFile(
        fileName: String? = null,
        mimeType: String? = null,
        newQ: String? = null
    ): List<GDriveFileWrapper> = withContext(ioDispatcher) {
        val files = mutableListOf<GDriveFileWrapper>()
        var newPageToken: String? = null
        val service = getDriveService()
        if (service != null) {
            do {
                var query: String? = null
                fileName?.let {
                    query = "name=\"$fileName\""
                }
                mimeType?.let {
                    query = if (query == null) {
                        "mimeType='$it'"
                    } else {
                        "$query and mimeType = '$it'"
                    }
                }
                newQ?.let {
                    query = if (query == null) {
                        "$newQ"
                    } else {
                        "$query and $newQ"
                    }
                }
                val request = service.files().list().apply {
                    fields = DEFAULT_FIELDS
                    q = query
                    pageToken = newPageToken
                }

                val result = request.execute()
                val wrapper = result.files.map { file ->
                    val holder = gDriveFileMapper.mapToWrapper(file)
                    Timber.d("foundFile: $holder")
                    holder
                }

                files.addAll(wrapper)
                newPageToken = result.nextPageToken
            } while (newPageToken != null)
        }
        return@withContext files
    }

    private suspend fun createFolder(
        name: String,
        isRoot: Boolean = false
    ): String? {
        val googleDrive = getDriveService()
        if (googleDrive != null) {
            try {
                val folderData = com.google.api.services.drive.model.File()
                folderData.name = name
                folderData.mimeType = DriveFolder.MIME_TYPE
                if (isRoot.not()) {
                    folderData.parents = listOf(getRootFolderId())
                }
                val folder = googleDrive
                    .files()
                    .create(folderData)
                    .execute()
                Timber.d("created folder $name id: $folder")
                return folder.id
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        return null
    }

    override suspend fun uploadFiles(
        localDocs: List<Document>
    ): Try<List<Document>, Throwable> = supervisorScope {
        runOperationCatching {
            localDocs.map { document: Document ->
                val searchForFolder = searchDocumentFolder(document)
                async {
                    Timber.d("uploadFiles document: $document")
                    if (searchForFolder.isEmpty()) {
                        val documentFolderId = getDocumentFolderId(document)
                        uploadDocJson(document, documentFolderId)

                        document.filesUri.map { documentFileData: DocumentFileData ->
                            async {
                                Timber.d("uploading $documentFileData")
                                val file = fileUtils.createFileFromDocumentFileUri(
                                    document = document,
                                    documentFileData = documentFileData
                                )
                                Timber.d("uploading data: $documentFileData")
                                uploadFileToGDrive(
                                    file = file,
                                    type = documentFileData.mimeType,
                                    documentParent = documentFolderId
                                )
                            }
                        }.awaitAll()
                    }
                    document
                }
            }.awaitAll()
        }
    }

    private suspend fun uploadDocJson(
        document: Document,
        documentFolderId: String?
    ) = withContext(ioDispatcher) {
        val docJson = json.encodeToString(document.toUploadDTO())
        Timber.d("uploading json: $docJson")
        val folder = fileUtils.getDocumentFolderName(document)

        val jsonFile = File(
            fileUtils.getFolder(folder),
            "$folder.json"
        )

        jsonFile.writeBytes(docJson.toByteArray())

        val createdDate = dateTimeUtils.formatToGDrive(document.createdDate)

        uploadFileToGDrive(
            file = jsonFile,
            type = MimeTypes.Application.JSON,
            documentParent = documentFolderId,
            fileAppProperties = mapOf(
                JSON_APP_PROPERTY_NAME to fileUtils.getDocumentFolderName(document),
                JSON_APP_ID to "${document.id}",
                JSON_APP_DATE to createdDate
            )
        )
    }

    private fun uploadFileToGDrive(
        file: File,
        type: String,
        documentParent: String?,
        fileAppProperties: Map<String, String>? = null
    ): com.google.api.services.drive.model.File? {
        getDriveService()?.let { googleDriveService ->
            try {
                val gfile = com.google.api.services.drive.model.File()
                gfile.name = file.name
                documentParent?.let {
                    gfile.parents = listOf(it)
                }
                fileAppProperties?.let {
                    gfile.appProperties = it
                }
                val fileContent = FileContent(type, file)
                val result = googleDriveService
                    .Files()
                    .create(gfile, fileContent)
                    .execute()

                Timber.d("File has been uploaded successfully: $result")
                return result
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        return null
    }

    suspend fun downloadFiles(
        localDocs: List<Document>
    ): List<LocalGDriveParentFile> = withContext(ioDispatcher) {
        val filesToSave = mutableListOf<LocalGDriveParentFile>()
        getDriveService()?.let { googleDriveService ->
            var newPageToken: String?
            do {
                val result = googleDriveService.files().list().apply {
                    fields = DEFAULT_FIELDS
                    newPageToken = pageToken
                    q = "mimeType != 'application/vnd.google-apps.folder' " +
                            "and mimeType = '${MimeTypes.Application.JSON}' " +
                            "and name contains 'json'"
                }.execute()

                result.files.map {
                    gDriveFileMapper.mapToWrapper(it)
                }.map { file: GDriveFileWrapper ->
                    async {
                        Timber.d("mapped file: $file")

                        val docFolderName = file.appProperties?.get(JSON_APP_PROPERTY_NAME)!!
                        val localFile = File(
                            fileUtils.getFolder(docFolderName),
                            file.name
                        )
                        if (!localFile.exists()) {
                            Timber.d("start syncing json file: $file")
                            val outputStream = downloadFileFromGDrive(file.gDriveFileId)
                            outputStream?.use { baos ->
                                localFile.outputStream().use { fos ->
                                    baos.writeTo(fos)
                                }
                            }
                            val docFiles = downloadFilesFromDocument(
                                file,
                                docFolderName
                            )
                            val fileUri = fileUtils.getFileUri(
                                docFolderName,
                                localFile.name
                            )

                            val uploadDTOString = localFile.readText()
                            val decodedUploadDTO = json
                                .decodeFromString<DocumentUploadDTO>(uploadDTOString)

                            val fileToSave = LocalGDriveParentFile(
                                id = decodedUploadDTO.id,
                                group = Group(
                                    id = decodedUploadDTO.group.id,
                                    name = decodedUploadDTO.group.name,
                                    fields = emptyList(),
                                    color = decodedUploadDTO.group.color
                                ),
                                name = file.name,
                                mimeType = file.mimeType!!,
                                children = docFiles,
                                md5 = hashUtils.md5(localFile),
                                size = localFile.length(),
                                file = localFile,
                                fileUri = fileUri,
                                appProperties = file.appProperties,
                                createdTime = file.createdTime,
                                modifiedTime = file.modifiedTime
                            )
                            filesToSave.add(fileToSave)
                        }
                    }
                }.awaitAll()

            } while (newPageToken != null)
        }
        filesToSave.toList()
    }

    private suspend fun downloadFilesFromDocument(
        gDriveFileWrapper: GDriveFileWrapper,
        docFolderName: String
    ): List<LocalGDriveChildFile> = withContext(ioDispatcher) {
        val filesToSave = mutableListOf<LocalGDriveChildFile>()

        getDriveService()?.let { googleDriveService ->
            var newPageToken: String?
            do {
                val result = googleDriveService.files().list().apply {
                    fields = DEFAULT_FIELDS
                    newPageToken = pageToken
                    q = "mimeType != '${DriveFolder.MIME_TYPE}' " +
                            "and '${gDriveFileWrapper.parents!!.first()}' in parents " +
                            "and not name contains '.json'"
                }.execute()

                result.files
                    .map {
                        gDriveFileMapper.mapToWrapper(it)
                    }.map { file: GDriveFileWrapper ->
                        async {
                            Timber.d("mapped sub file: $file")
                            val localFile = File(
                                fileUtils.getFolder(docFolderName),
                                file.name
                            )

                            if (localFile.exists().not()) {
                                Timber.d("start sync file $file")
                                val outputStream = downloadFileFromGDrive(file.gDriveFileId)
                                outputStream?.use { baos ->
                                    localFile.outputStream().use { fos ->
                                        baos.writeTo(fos)
                                    }
                                }

                                val fileUri = fileUtils.getFileUri(
                                    docFolderName,
                                    localFile.name
                                )
                                val filePreviewUri = fileUtils.getFilePreview(
                                    fileUri,
                                    docFolderName,
                                    file.mimeType!!
                                )

                                val fileToSave = LocalGDriveChildFile(
                                    name = file.name,
                                    mimeType = file.mimeType!!,
                                    md5 = hashUtils.md5(localFile),
                                    size = localFile.length(),
                                    file = localFile,
                                    fileUri = fileUri,
                                    filePreviewUriPath = filePreviewUri?.path,
                                    filePreviewUriString = filePreviewUri?.toString()
                                )
                                filesToSave.add(fileToSave)
                            }
                        }
                    }.awaitAll()
            } while (newPageToken != null)
        }

        filesToSave.toList()
    }

    private fun downloadFileFromGDrive(id: String): ByteArrayOutputStream? {
        val drive = getDriveService()
        if (drive != null) {
            try {
                val outputStream = ByteArrayOutputStream()
                drive.files().get(id).executeMediaAndDownloadTo(outputStream)
                return outputStream
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        return null
    }

    suspend fun accessDriveFiles() = withContext(Dispatchers.IO) {
        getDriveService()?.let { googleDriveService ->
            var pageToken: String? = null
            do {
                val result = googleDriveService.files().list().apply {
                    fields = DEFAULT_FIELDS
                    pageToken = this.pageToken
                }.execute()
                Timber.d("accessDriveFiles result: ${result.files.size}")
                result.files.forEach { resultFile ->
                    Timber.d("name=${resultFile.name}, id=${resultFile.id}, mimeType=${resultFile.mimeType}")
                }
            } while (pageToken != null)
        }
    }

}