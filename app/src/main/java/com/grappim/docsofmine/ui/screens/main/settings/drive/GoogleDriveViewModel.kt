package com.grappim.docsofmine.ui.screens.main.settings.drive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.grappim.docsofmine.data.gdrive.GoogleDriveManager
import com.grappim.docsofmine.data.worker.WorkerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoogleDriveViewModel @Inject constructor(
    private val googleDriveManager: GoogleDriveManager,
    private val workerHelper: WorkerHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoogleAuthState())
    val uiState: StateFlow<GoogleAuthState> = _uiState.asStateFlow()

    init {
        initializeSignIn()
    }

    private fun initializeSignIn() {
        viewModelScope.launch {
            val account = googleDriveManager.getLastSignedInAccount()
            _uiState.update { it.copy(currentUser = account, isUserSignIn = account != null) }
        }
    }

    fun updateAccountInfo(account: GoogleSignInAccount?) {
        _uiState.update { it.copy(currentUser = account, isUserSignIn = account != null) }
    }

    fun logOut() {
        googleDriveManager.getGoogleSignInClient().signOut()
        initializeSignIn()
    }

    fun upload() {
        workerHelper.startDriveUploading()
    }

    fun download() {
        workerHelper.startDriveDownloading()
    }

    fun accessFiles() {
        viewModelScope.launch {
            googleDriveManager.accessDriveFiles()
        }
    }
}