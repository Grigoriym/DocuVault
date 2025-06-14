package com.grappim.docuvault.utils.androidimpl.intent

import android.content.Intent
import com.grappim.docuvault.testing.getRandomUri
import com.grappim.docuvault.utils.androidapi.intent.IntentGenerator
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
internal class IntentGeneratorImplTest {
    private lateinit var sut: IntentGenerator

    @Before
    fun setup() {
        sut = IntentGeneratorImpl()
    }

    @Test
    fun `on getOpenFileIntent should return intent with action ACTION_VIEW`() {
        val uri = getRandomUri()
        val mimeType = "image/jpeg"
        val actual = sut.getOpenFileIntent(uri, mimeType)

        assertEquals(Intent.ACTION_VIEW, actual.action)
        assertEquals(uri, actual.data)
        assertEquals(mimeType, actual.type)
        assertEquals(Intent.FLAG_GRANT_READ_URI_PERMISSION, actual.flags)
    }
}
