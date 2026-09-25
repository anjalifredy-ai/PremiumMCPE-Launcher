package com.premiummcpe.launcher.data.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.premiummcpe.launcher.data.model.XboxAccount

object AuthState {
    var currentAccount: XboxAccount? by mutableStateOf(null)
        private set

    val isSignedIn: Boolean
        get() = currentAccount != null

    fun completeSignIn(gamertag: String, email: String? = null) {
        currentAccount = XboxAccount(
            id = "ms-${System.currentTimeMillis()}",
            gamertag = gamertag.ifBlank { "Xbox Player" },
            email = email,
            isActive = true
        )
    }

    fun signOut() {
        currentAccount = null
    }
}
