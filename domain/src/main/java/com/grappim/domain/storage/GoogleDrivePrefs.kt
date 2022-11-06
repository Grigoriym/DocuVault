package com.grappim.domain.storage

interface GoogleDrivePrefs {
    var googleDriveRootFolder: String?
    var lastSyncTime: String?
}