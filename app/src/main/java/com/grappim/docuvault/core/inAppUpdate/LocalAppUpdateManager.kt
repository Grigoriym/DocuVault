package com.grappim.docuvault.core.inAppUpdate

import androidx.compose.runtime.staticCompositionLocalOf
import com.google.android.play.core.appupdate.AppUpdateManager

val LocalAppUpdateManager = staticCompositionLocalOf<AppUpdateManager?> {
    null
}
