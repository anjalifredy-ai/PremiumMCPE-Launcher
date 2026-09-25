package com.premiummcpe.launcher.data.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.premiummcpe.launcher.data.model.XboxAccount

/**
 * Global auth state for the launcher.
 * Launch is blocked until the user signs in with Microsoft / Xbox.
 * Real OAuth will replace [signInDemo] later — this gates UI legally:
 * user must own Minecraft; launcher just requires account before play.
 */
object AuthState {
    var currentAccount: XboxAccount? by mutableStateOf(null)
        private set

    val isSignedIn: Boolean
        get() = currentAccount != null

    /** Demo sign-in for UI flow. Replace with Microsoft OAuth + token store. */
    fun signInDemo(gamertag: String = "Player", email: String? = null) {
        currentAccount = XboxAccount(
            id = "demo-${System.currentTimeMillis()}",
            gamertag = gamertag.ifBlank { "Xbox Player" },
            email = email,
            isActive = true
        )
    }

    fun signOut() {
        currentAccount = null
    }
}
