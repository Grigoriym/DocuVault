package com.grappim.docsofmine.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.grappim.docsofmine.data.utils.string
import com.grappim.domain.storage.GoogleDrivePrefs
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleDrivePrefsImpl @Inject constructor(
    @ApplicationContext context: Context
) : GoogleDrivePrefs {

    companion object {
        private const val GOOGLE_DRIVE_ROOT_FOLDER = "google.drive.root.folder"
        private const val GOOGLE_DRIVE_LAST_SYNC_TIME = "google.drive.last.sync.time"
    }

    private val sharedPrefs: SharedPreferences = EncryptedSharedPreferences.create(
        "docs_of_mine_prefs",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override var googleDriveRootFolder: String? by sharedPrefs.string(GOOGLE_DRIVE_ROOT_FOLDER)
    override var lastSyncTime: String? by sharedPrefs.string(GOOGLE_DRIVE_LAST_SYNC_TIME)


}