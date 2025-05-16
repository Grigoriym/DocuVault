package com.grappim.docuvault.utils.files.di

import com.grappim.docuvault.utils.files.UriParser
import com.grappim.docuvault.utils.files.UriParserImpl
import com.grappim.docuvault.utils.files.creation.FileCreationUtilsImpl
import com.grappim.docuvault.utils.files.deletion.FileDeletionUtilsImpl
import com.grappim.docuvault.utils.files.docfilemanager.DocumentFileManagerImpl
import com.grappim.docuvault.utils.files.docfiles.FilesPersistenceManagerImpl
import com.grappim.docuvault.utils.files.inforetriever.FileInfoRetrieverImpl
import com.grappim.docuvault.utils.files.mappers.DocsListUIMapperImpl
import com.grappim.docuvault.utils.files.mappers.FileDataMapperImpl
import com.grappim.docuvault.utils.files.pathmanager.FolderPathManagerImpl
import com.grappim.docuvault.utils.files.transfer.FileTransferOperationsImpl
import com.grappim.docuvault.utils.files.urimanager.FileUriManagerImpl
import com.grappim.docuvault.utils.filesapi.FilesPersistenceManager
import com.grappim.docuvault.utils.filesapi.creation.FileCreationUtils
import com.grappim.docuvault.utils.filesapi.deletion.FileDeletionUtils
import com.grappim.docuvault.utils.filesapi.docfilemanager.DocumentFileManager
import com.grappim.docuvault.utils.filesapi.inforetriever.FileInfoRetriever
import com.grappim.docuvault.utils.filesapi.mappers.DocsListUIMapper
import com.grappim.docuvault.utils.filesapi.mappers.FileDataMapper
import com.grappim.docuvault.utils.filesapi.pathmanager.FolderPathManager
import com.grappim.docuvault.utils.filesapi.transfer.FileTransferOperations
import com.grappim.docuvault.utils.filesapi.urimanager.FileUriManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
interface FileUtilsModule {
    @Binds
    fun bindFileUriManager(fileUriManagerImpl: FileUriManagerImpl): FileUriManager

    @Binds
    fun bindFolderPathManager(folderPathManagerImpl: FolderPathManagerImpl): FolderPathManager

    @Binds
    fun bindFileInfoRetriever(fileInfoRetrieverImpl: FileInfoRetrieverImpl): FileInfoRetriever

    @Binds
    fun bindFileCreationUtils(fileCreationUtilsImpl: FileCreationUtilsImpl): FileCreationUtils

    @Binds
    fun bindUriParser(uriParserImpl: UriParserImpl): UriParser

    @Binds
    fun bindFileDeletionUtils(fileDeletionUtilsImpl: FileDeletionUtilsImpl): FileDeletionUtils

    @Binds
    fun bindFileDataMapper(fileDataMapperImpl: FileDataMapperImpl): FileDataMapper

    @Binds
    fun bindDocumentListUIMapper(docsListUIMapperImpl: DocsListUIMapperImpl): DocsListUIMapper

    @Binds
    fun bindDocumentFileManager(
        documentFileManagerImpl: DocumentFileManagerImpl
    ): DocumentFileManager

    @Binds
    fun bindFileTransferOperations(
        fileTransferOperationsImpl: FileTransferOperationsImpl
    ): FileTransferOperations

    @Binds
    fun bindFilesPersistenceManager(
        docFilesPersistenceManagerImpl: FilesPersistenceManagerImpl
    ): FilesPersistenceManager
}
