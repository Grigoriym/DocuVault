package com.grappim.docuvault.utils.androidapi.intent

import android.content.Intent
import android.net.Uri

interface IntentGenerator {
    fun getOpenFileIntent(uri: Uri, mimeType: String): Intent
}
