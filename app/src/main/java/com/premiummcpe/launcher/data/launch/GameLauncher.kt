package com.premiummcpe.launcher.data.launch

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

object GameLauncher {

    const val PKG_RELEASE = "com.mojang.minecraftpe"
    const val PKG_PREVIEW = "com.mojang.minecraftpe.beta"

    data class LaunchResult(val ok: Boolean, val message: String)

    fun isMinecraftInstalled(context: Context, preview: Boolean = false): Boolean {
        val pkg = if (preview) PKG_PREVIEW else PKG_RELEASE
        return try {
            context.packageManager.getPackageInfo(pkg, 0)
            true
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun launchMinecraft(context: Context, preview: Boolean = false): LaunchResult {
        val pkg = if (preview) PKG_PREVIEW else PKG_RELEASE
        val pm = context.packageManager
        val launch = pm.getLaunchIntentForPackage(pkg)
        return if (launch != null) {
            launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                context.startActivity(launch)
                LaunchResult(true, "Launching Minecraft…")
            } catch (e: Exception) {
                LaunchResult(false, e.message ?: "Launch failed")
            }
        } else {
            try {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$pkg"))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            } catch (_: Exception) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$pkg")
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
            LaunchResult(
                false,
                "Minecraft not installed. Install official game from Play Store (own it), then Play again."
            )
        }
    }

    fun openPackWithMinecraft(context: Context, uri: Uri): LaunchResult {
        return try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/octet-stream")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                setPackage(PKG_RELEASE)
            }
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
                LaunchResult(true, "Opening pack in Minecraft…")
            } else {
                val any = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "*/*")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(any, "Open pack with Minecraft"))
                LaunchResult(true, "Choose Minecraft to import pack")
            }
        } catch (e: Exception) {
            LaunchResult(false, e.message ?: "Could not open pack")
        }
    }
}
