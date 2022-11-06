package com.grappim.docsofmine.data.gdrive

import android.content.Context
import android.net.Uri
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
import com.grappim.docsofmine.data.mappers.toDTO
import com.grappim.docsofmine.data.runOperationCatching
import com.grappim.docsofmine.uikit.R
import com.grappim.docsofmine.utils.datetime.DateTimeUtils
import com.grappim.docsofmine.utils.datetime.DtfGDriveDocumentFolder
import com.grappim.domain.Document
import com.grappim.domain.DocumentFileUri
import com.grappim.domain.storage.GoogleDrivePrefs
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleDriveManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val googleDrivePrefs: GoogleDrivePrefs,
    private val fileUtils: com.grappim.docsofmine.utils.files.FileUtils,
    private val dateTimeUtils: DateTimeUtils,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @DtfGDriveDocumentFolder private val dtfGDriveDocumentFolder: DateTimeFormatter,
    private val json: Json
) {

    /**
     * https://developers.google.com/drive/api/v3/reference/files
     */
    companion object {
        private const val DEFAULT_FIELDS =
            "nextPageToken, files(id,name,size,createdTime,modifiedTime," +
                    "starred,mimeType,appProperties,parents)"

        private const val JSON_APP_PROPERTY_NAME = "json.app.property.name"
        private const val JSON_APP_PROPERTY_ENTITY_ID = "json.app.property.entity.id"
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
            ).firstOrNull()?.id
            if (folderId == null) {
                folderId = createFolder(
                    name = context.getString(R.string.app_name),
                    isRoot = true
                )
            }
        }
        return folderId
    }

    private fun getDocumentFolderName(
        document: Document
    ): String =
        "${document.id}_${dateTimeUtils.getDateForDocumentGoogleDriveFolder(document.createdDate)}"

    private suspend fun getDocumentFolderId(
        document: Document
    ): String? {
        val documentFolderName = getDocumentFolderName(document)
        var folderId = searchFile(
            fileName = documentFolderName,
            mimeType = DriveFolder.MIME_TYPE
        ).firstOrNull()?.id
        if (folderId == null) {
            folderId = createFolder(documentFolderName)
        }
        return folderId
    }

    private suspend fun searchForDocumentFolder(
        document: Document
    ): List<com.grappim.docsofmine.gdrive.GDriveFileHolder> {
        val documentFolderName = getDocumentFolderName(document)
        return searchFile(
            fileName = documentFolderName,
            mimeType = DriveFolder.MIME_TYPE
        )
    }

    private suspend fun searchFile(
        fileName: String? = null,
        mimeType: String? = null,
        newQ: String? = null
    ): List<com.grappim.docsofmine.gdrive.GDriveFileHolder> = withContext(ioDispatcher) {
        val files = mutableListOf<com.grappim.docsofmine.gdrive.GDriveFileHolder>()
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
                    val holder = mapToGDriveFileHolder(file)
                    Timber.d("foundFile: $holder")
                    holder
                }

                files.addAll(wrapper)
                newPageToken = result.nextPageToken
            } while (newPageToken != null)
        }

        return@withContext files
    }

    private fun mapToGDriveFileHolder(
        file: com.google.api.services.drive.model.File
    ): com.grappim.docsofmine.gdrive.GDriveFileHolder {
        val createdTime = if (file.createdTime != null) {
            dateTimeUtils.getDateFromGDrive(file.createdTime.toStringRfc3339())
        } else {
            null
        }
        val modifiedTime = if (file.modifiedTime != null) {
            dateTimeUtils.getDateFromGDrive(file.modifiedTime.toStringRfc3339())
        } else {
            null
        }
        return com.grappim.docsofmine.gdrive.GDriveFileHolder(
            name = file.name,
            id = file.id,
            size = file.size.toLong(),
            mimeType = file.mimeType,
            createdTime = createdTime,
            modifiedTime = modifiedTime,
            starred = file.starred ?: false,
            appProperties = file.appProperties,
            parents = file.parents
        )
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
                Timber.d("created folder id: $folder")
                return folder.id
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        return null
    }

    suspend fun uploadFiles(
        list: List<Document>
    ) = supervisorScope {
        runOperationCatching {
            list.map { document: Document ->
                val searchForFolder = searchForDocumentFolder(document)
                async {
                    Timber.d("uploadFiles document: $document")

                    Timber.d("uploadFiles searchForFolder: ${searchForFolder.joinToString()}")
                    if (searchForFolder.isEmpty()) {
                        val documentFolderId = getDocumentFolderId(document)
                        uploadDocJson(document, documentFolderId)

                        document.filesUri.map { documentFileUri: DocumentFileUri ->
                            async {
                                Timber.d("uploading $documentFileUri, $document")
                                val uri = Uri.parse(documentFileUri.string)
                                val file = fileUtils.createFileFromDocumentFileUri(
                                    document = document,
                                    documentFileUri = documentFileUri
                                )
                                val mimeType = documentFileUri.mimeType
                                Timber.d("uploading data: $uri, $file, $mimeType")
                                uploadFileToGDrive(
                                    file = file,
                                    type = mimeType,
                                    documentParent = documentFolderId
                                )
                            }
                        }.awaitAll()
                    }
                }
            }.awaitAll()
        }
    }

    private fun getLocalFolder(
        child: String
    ): File {
        val folder = File(context.filesDir, "docs/$child")
        if (folder.exists().not()) {
            folder.mkdirs()
        }
        return folder
    }

    private suspend fun uploadDocJson(
        document: Document,
        documentFolderId: String?
    ) = withContext(ioDispatcher) {
        val docJson = json.encodeToString(document.toDTO())
        Timber.d("uploading json: $docJson")
        val folder = document.getGDriveFileName(
            dateTimeUtils.formatToGDrive(document.createdDate)
        )

        val jsonFile = File(
            getLocalFolder(folder),
            "$folder.json"
        )

        jsonFile.writeBytes(docJson.toByteArray())

        val appPropertyName = dtfGDriveDocumentFolder.format(document.createdDate)

        uploadFileToGDrive(
            file = jsonFile,
            type = "application/json",
            documentParent = documentFolderId,
            fileAppProperties = mapOf(
                JSON_APP_PROPERTY_NAME to "${document.id}_${appPropertyName}"
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
                val result = googleDriveService.Files().create(gfile, fileContent).execute()

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
    ) = withContext(ioDispatcher) {
        Timber.d("downloadFiles")
        getDriveService()?.let { googleDriveService ->
            var newPageToken: String?
            do {
                val result = googleDriveService.files().list().apply {
                    fields = DEFAULT_FIELDS
                    newPageToken = pageToken
                    q = "mimeType != 'application/vnd.google-apps.folder' " +
                            "and mimeType = 'application/json' " +
                            "and name contains 'json'"
                }.execute()

                result.files.map {
                    mapToGDriveFileHolder(it)
                }.map { file: com.grappim.docsofmine.gdrive.GDriveFileHolder ->
                    async {
                        Timber.d("mapped file: $file")

                        val folderName = file.appProperties?.get(JSON_APP_PROPERTY_NAME)!!
                        val localFile = File(
                            fileUtils.getFolder(folderName),
                            file.name
                        )
                        if (!localFile.exists()) {
                            Timber.d("start syncing json file: $file")
                            val outputStream = downloadFileFromGDrive(file.id)
                            outputStream?.use { baos ->
                                localFile.outputStream().use { fos ->
                                    baos.writeTo(fos)
                                }
                            }
                            downloadFilesFromDocument(file)
                        }
                    }
                }.awaitAll()

            } while (newPageToken != null)
        }
    }

    private suspend fun downloadFilesFromDocument(
        gDriveFileHolder: com.grappim.docsofmine.gdrive.GDriveFileHolder
    ) = withContext(ioDispatcher) {
        getDriveService()?.let { googleDriveService ->
            var newPageToken: String?
            do {
                val result = googleDriveService.files().list().apply {
                    fields = DEFAULT_FIELDS
                    newPageToken = pageToken
                    q = "mimeType != 'application/vnd.google-apps.folder' " +
                            "and '${gDriveFileHolder.parents!!.first()}' in parents"
                }.execute()

                result.files
                    .map {
                        mapToGDriveFileHolder(it)
                    }.map { file: com.grappim.docsofmine.gdrive.GDriveFileHolder ->
                        async {
                            Timber.d("mapped sub file: $file")

                            val folderName =
                                gDriveFileHolder.appProperties?.get(JSON_APP_PROPERTY_NAME)!!
                            val localFile = File(
                                fileUtils.getFolder(folderName),
                                file.name
                            )

                            if (localFile.exists().not()) {
                                Timber.d("start sync file $file")
                                val outputStream = downloadFileFromGDrive(file.id)
                                outputStream?.use { baos ->
                                    localFile.outputStream().use { fos ->
                                        baos.writeTo(fos)
                                    }
                                }
                            }
                        }
                    }.awaitAll()
            } while (newPageToken != null)
        }
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