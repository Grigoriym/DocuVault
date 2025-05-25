package com.grappim.docuvault.testing

import com.grappim.docuvault.feature.docs.domain.CreateDocument
import com.grappim.docuvault.feature.docs.domain.DocumentFile

fun getCreateDocument(): CreateDocument = CreateDocument(
    id = getRandomLong(),
    name = getRandomString(),
    group = getGroup(),
    files = listOf(
        getDocumentFileData(),
        getDocumentFileData()
    ),
    createdDate = nowDate,
    documentFolderName = getRandomString()
)

fun getDocumentFileData(): DocumentFile = DocumentFile(
    fileId = getRandomLong(),
    name = getRandomString(),
    mimeType = getRandomString(),
    uriString = getRandomString(),
    size = getRandomLong(),
    md5 = getRandomString()
)
