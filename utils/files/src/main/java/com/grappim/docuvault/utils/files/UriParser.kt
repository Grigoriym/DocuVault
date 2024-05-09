package com.grappim.docuvault.utils.files

import android.net.Uri
import javax.inject.Inject

interface UriParser {
    fun parse(uriString: String): Uri
}

class UriParserImpl @Inject constructor() : UriParser {
    override fun parse(uriString: String): Uri = Uri.parse(uriString)
}
