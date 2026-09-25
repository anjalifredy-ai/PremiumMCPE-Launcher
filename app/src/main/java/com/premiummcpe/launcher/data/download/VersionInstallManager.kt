package com.premiummcpe.launcher.data.download

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Mojo-style install state for each Bedrock version id.
 *
 * Real Bedrock APKs are not served like Java jars from Mojang's public CDN.
 * This manager tracks NotInstalled → Downloading(progress) → Installed.
 * Does NOT ship pirated APK URLs.
 */
sealed class InstallStatus {
    data object NotInstalled : InstallStatus()
    data class Downloading(val progress: Float) : InstallStatus()
    data object Installed : InstallStatus()
    data class Failed(val message: String) : InstallStatus()
}

object VersionInstallManager {
    val statusMap: SnapshotStateMap<String, InstallStatus> = mutableStateMapOf()
    private val jobs = mutableMapOf<String, Job>()
    private val scope = CoroutineScope(Dispatchers.Main)

    fun status(versionId: String): InstallStatus =
        statusMap[versionId] ?: InstallStatus.NotInstalled

    fun isInstalled(versionId: String): Boolean =
        status(versionId) is InstallStatus.Installed

    fun startDownload(versionId: String) {
        if (status(versionId) is InstallStatus.Downloading) return
        if (status(versionId) is InstallStatus.Installed) return

        jobs[versionId]?.cancel()
        statusMap[versionId] = InstallStatus.Downloading(0f)

        jobs[versionId] = scope.launch {
            try {
                for (i in 1..20) {
                    delay(100)
                    statusMap[versionId] = InstallStatus.Downloading(i / 20f)
                }
                statusMap[versionId] = InstallStatus.Installed
            } catch (e: Exception) {
                statusMap[versionId] = InstallStatus.Failed(e.message ?: "Download failed")
            }
        }
    }

    fun cancel(versionId: String) {
        jobs[versionId]?.cancel()
        jobs.remove(versionId)
        if (status(versionId) is InstallStatus.Downloading) {
            statusMap[versionId] = InstallStatus.NotInstalled
        }
    }

    fun markInstalledFromImport(versionId: String) {
        jobs[versionId]?.cancel()
        statusMap[versionId] = InstallStatus.Installed
    }

    fun uninstall(versionId: String) {
        jobs[versionId]?.cancel()
        statusMap[versionId] = InstallStatus.NotInstalled
    }
}
