package com.premiummcpe.launcher.data.auth

import android.content.Context
import android.content.Intent
import android.net.Uri

object MicrosoftAuth {
    fun openMicrosoftLogin(context: Context) {
        val url = "https://login.live.com/login.srf?wa=wsignin1.0&wreply=https%3A%2F%2Faccount.microsoft.com%2F"
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    fun openXboxLogin(context: Context) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("https://www.xbox.com/auth/msa?action=logIn"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
