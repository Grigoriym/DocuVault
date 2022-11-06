package com.grappim.docsofmine.core.inAppUpdate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.ktx.AppUpdateResult
import com.google.android.play.core.ktx.requestUpdateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@Composable
fun rememberMutableInAppUpdateState(): InAppUpdateState {
    val context = LocalContext.current
    val appUpdateManager = LocalAppUpdateManager.current ?: AppUpdateManagerFactory.create(context)
    val scope = rememberCoroutineScope()

    val inAppUpdateState = remember {
        MutableInAppUpdateState(scope, appUpdateManager)
    }
    return inAppUpdateState
}

class MutableInAppUpdateState(
    scope: CoroutineScope,
    private val appUpdateManager: AppUpdateManager
) : InAppUpdateState {
    private var _appUpdateResult by mutableStateOf<AppUpdateResult>(AppUpdateResult.NotAvailable)

    override val appUpdateResult: AppUpdateResult
        get() = _appUpdateResult

    init {
        scope.launch {
            appUpdateManager.requestUpdateFlow().catch { error ->

            }.collect {
                _appUpdateResult = it
            }
        }
    }
}