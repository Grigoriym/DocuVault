package com.grappim.docuvault.repo

import com.grappim.docuvault.feature.group.domain.Group
import com.grappim.docuvault.testing.getRandomLong
import com.grappim.docuvault.testing.getRandomString
import com.grappim.docuvault.testing.nowDate
import com.grappim.domain.model.document.CreateDocument
import com.grappim.domain.model.document.DocumentFileData

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

fun getGroup(): Group = Group(
    id = getRandomLong(),
    name = getRandomString(),
    fields = listOf(),
    color = getRandomString()
)

fun getDocumentFileData(): DocumentFileData = DocumentFileData(
    fileId = getRandomLong(),
    name = getRandomString(),
    mimeType = getRandomString(),
    uriString = getRandomString(),
    size = getRandomLong(),
    md5 = getRandomString()
)
