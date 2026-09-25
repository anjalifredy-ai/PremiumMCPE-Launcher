package com.premiummcpe.launcher.data.auth

import android.content.Context
import android.content.Intent
import android.net.Uri

object MicrosoftAuth {
    const val CLIENT_ID = "00000000-0000-0000-0000-000000000000"

    fun openMicrosoftLogin(context: Context) {
        val url = "https://login.live.com/login.srf?wa=wsignin1.0"
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    fun openXboxProfile(context: Context) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("https://www.xbox.com/play"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
