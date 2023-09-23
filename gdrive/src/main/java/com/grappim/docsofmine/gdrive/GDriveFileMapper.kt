package com.grappim.docsofmine.gdrive

import com.google.api.services.drive.model.File
import com.grappim.docsofmine.utils.dateTime.DateTimeUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GDriveFileMapper @Inject constructor(
    private val dateTimeUtils: DateTimeUtils
) {

    fun mapToWrapper(file: File): GDriveFileWrapper {
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
        return GDriveFileWrapper(
            gDriveFileId = file.id,
            name = file.name,
            mimeType = file.mimeType,
            createdTime = createdTime,
            modifiedTime = modifiedTime,
            appProperties = file.appProperties,
            parents = file.parents
        )
    }
}