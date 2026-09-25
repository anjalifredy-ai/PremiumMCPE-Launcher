package com.premiummcpe.launcher.data.launch

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.premiummcpe.launcher.data.download.VersionStorage
import java.io.File

/**
 * 1) Microsoft sign-in in launcher (UI gate)
 * 2) Official APK into version slot (user import — not Play Store download)
 * 3) PLAY installs/launches THAT APK from launcher storage
 */
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

    fun launchVersion(context: Context, versionId: String, preview: Boolean = false): LaunchResult {
        val apk = VersionStorage.apkFile(context, versionId)
        if (!apk.exists() || apk.length() < 1024L) {
            return LaunchResult(
                false,
                "Is version ka APK launcher mein nahi. Versions → Install → official Minecraft APK. Play Store auto-download nahi."
            )
        }
        val installResult = installApkFromFile(context, apk)
        if (!installResult.ok) {
            val launched = launchInstalledPackage(context, preview)
            return if (launched.ok) {
                LaunchResult(true, "Opening game package…")
            } else installResult
        }
        return launchInstalledPackage(context, preview).let {
            if (it.ok) LaunchResult(true, "Launching game…")
            else LaunchResult(true, "Install dialog open — Allow ke baad dubara PLAY")
        }
    }

    fun launchDefaultFromLauncher(context: Context): LaunchResult {
        val ids = VersionStorage.listInstalledIds(context)
        if (ids.isEmpty()) {
            return LaunchResult(
                false,
                "Koi version launcher mein nahi. Versions → Install → official APK. Play Store open nahi hoga."
            )
        }
        val id = ids.first()
        val meta = VersionStorage.readMeta(context, id)
        val preview = meta?.packageName?.contains("beta") == true
        return launchVersion(context, id, preview)
    }

    fun launchInstalledPackage(context: Context, preview: Boolean = false): LaunchResult {
        val pkg = if (preview) PKG_PREVIEW else PKG_RELEASE
        val launch = context.packageManager.getLaunchIntentForPackage(pkg)
            ?: context.packageManager.getLaunchIntentForPackage(PKG_RELEASE)
            ?: context.packageManager.getLaunchIntentForPackage(PKG_PREVIEW)
        return if (launch != null) {
            launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                context.startActivity(launch)
                LaunchResult(true, "Launching game…")
            } catch (e: Exception) {
                LaunchResult(false, e.message ?: "Launch failed")
            }
        } else {
            LaunchResult(false, "Game package nahi mila. Versions se APK Install karo, phir PLAY.")
        }
    }

    fun installApkFromFile(context: Context, apk: File): LaunchResult {
        if (!apk.exists()) return LaunchResult(false, "APK missing")
        return try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                apk
            )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(intent)
            LaunchResult(true, "Install dialog — Allow, phir PLAY")
        } catch (e: Exception) {
            try {
                installWithSession(context, apk)
            } catch (e2: Exception) {
                LaunchResult(false, e.message ?: e2.message ?: "Install failed")
            }
        }
    }

    private fun installWithSession(context: Context, apk: File): LaunchResult {
        val installer = context.packageManager.packageInstaller
        val params = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
        val sessionId = installer.createSession(params)
        installer.openSession(sessionId).use { session ->
            session.openWrite("base.apk", 0, apk.length()).use { out ->
                apk.inputStream().use { input -> input.copyTo(out) }
                session.fsync(out)
            }
            val callback = Intent(context, InstallResultReceiver::class.java)
            val flags = PendingIntent.FLAG_UPDATE_CURRENT or
                (if (Build.VERSION.SDK_INT >= 31) PendingIntent.FLAG_MUTABLE else 0)
            val pi = PendingIntent.getBroadcast(context, sessionId, callback, flags)
            session.commit(pi.intentSender)
        }
        return LaunchResult(true, "Installing version APK…")
    }

    fun openPackWithMinecraft(context: Context, uri: Uri): LaunchResult {
        return try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/octet-stream")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Open with Minecraft"))
            LaunchResult(true, "Choose Minecraft to import pack")
        } catch (e: Exception) {
            LaunchResult(false, e.message ?: "Could not open pack")
        }
    }
}
