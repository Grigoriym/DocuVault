package com.grappim.docsofmine.ui.screens.main.settings.drive

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

data class GoogleAuthState(
    val isUserSignIn: Boolean = false,
    val currentUser: GoogleSignInAccount? = null,
    val syncDate: String? = null,
    val isLoading:Boolean = false
)