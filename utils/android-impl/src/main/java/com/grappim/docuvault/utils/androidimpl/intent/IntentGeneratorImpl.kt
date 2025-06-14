package com.grappim.docuvault.utils.androidimpl.intent

import android.content.Intent
import android.net.Uri
import com.grappim.docuvault.utils.androidapi.intent.IntentGenerator
import javax.inject.Inject

class IntentGeneratorImpl @Inject constructor() : IntentGenerator {

    override fun getOpenFileIntent(uri: Uri, mimeType: String): Intent = Intent(Intent.ACTION_VIEW)
        .setDataAndType(uri, mimeType)
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
}
