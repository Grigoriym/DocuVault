package com.grappim.docuvault.testing

import com.grappim.docuvault.feature.docgroup.repoapi.model.Group
import com.grappim.docuvault.feature.docs.db.model.DocumentEntity
import com.grappim.docuvault.feature.docs.db.model.DocumentFileEntity
import com.grappim.docuvault.feature.docs.db.model.FullDocumentEntity
import com.grappim.docuvault.feature.docs.repoapi.models.CreateDocument
import com.grappim.docuvault.feature.docs.repoapi.models.Document
import com.grappim.docuvault.feature.docs.repoapi.models.DocumentFile
import com.grappim.docuvault.feature.docs.repoapi.models.DraftDocument
import com.grappim.docuvault.feature.docs.uiapi.DocumentFileUI
import com.grappim.docuvault.feature.docs.uiapi.DocumentListUI

fun getCreateDocument(): CreateDocument = CreateDocument(
    id = getRandomLong(),
    name = getRandomString(),
    group = getGroup(),
    files = listOf(
        getDocumentFile(),
        getDocumentFile()
    ),
    createdDate = nowDate,
    documentFolderName = getRandomString()
)

fun getDocumentFile(): DocumentFile = DocumentFile(
    fileId = getRandomLong(),
    name = getRandomString(),
    mimeType = getRandomString(),
    uriString = getRandomString(),
    size = getRandomLong(),
    md5 = getRandomString()
)

fun getDocument(): Document = Document(
    documentId = getRandomLong(),
    name = getRandomString(),
    createdDate = nowDate,
    group = Group(
        id = getRandomLong(),
        name = getRandomString(),
        fields = emptyList(),
        color = getRandomString()
    ),
    files = emptyList(),
    documentFolderName = getRandomString()
)

fun getDocumentListUi(): DocumentListUI = DocumentListUI(
    id = getRandomString(),
    name = getRandomString(),
    createdDate = getRandomString(),
    groupColor = getRandomColor(),
    preview = listOf(getRandomString(), getRandomString())
)

fun getDocumentFileUI(): DocumentFileUI = DocumentFileUI(
    fileId = getRandomLong(),
    uri = getRandomUri(),
    name = getRandomString(),
    size = getRandomLong(),
    mimeType = getRandomString(),
    md5 = getRandomString()
)

fun getDocumentEntity(): DocumentEntity = DocumentEntity(
    documentId = getRandomLong(),
    name = getRandomString(),
    createdDate = nowDate,
    documentFolderName = getRandomString(),
    isCreated = getRandomBoolean(),
    groupId = getRandomLong()
)

fun getFullDocumentEntity(): FullDocumentEntity = FullDocumentEntity(
    documentEntity = getDocumentEntity(),
    documentFiles = emptyList(),
    group = getGroupEntity(),
    groupFields = emptyList()
)

fun getDocumentFileEntity(): DocumentFileEntity = DocumentFileEntity(
    fileId = getRandomLong(),
    name = getRandomString(),
    mimeType = getRandomString(),
    uriString = getRandomString(),
    size = getRandomLong(),
    md5 = getRandomString(),
    documentId = getRandomLong()
)

fun getDraftDocument(): DraftDocument = DraftDocument(
    id = getRandomLong(),
    date = nowDate,
    documentFolderName = getRandomString(),
    group = getGroup()
)
