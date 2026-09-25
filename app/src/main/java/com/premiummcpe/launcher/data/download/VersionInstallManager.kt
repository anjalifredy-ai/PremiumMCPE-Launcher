package com.premiummcpe.launcher.data.download

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class InstallStatus {
    data object NotInstalled : InstallStatus()
    data class Installing(val progress: Float) : InstallStatus()
    data object Installed : InstallStatus()
    data class Failed(val message: String) : InstallStatus()
}

object VersionInstallManager {
    val statusMap: SnapshotStateMap<String, InstallStatus> = mutableStateMapOf()
    private val jobs = mutableMapOf<String, Job>()
    private val scope = CoroutineScope(Dispatchers.Main)
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
        rescan()
    }

    fun rescan() {
        val ctx = appContext ?: return
        val installed = VersionStorage.listInstalledIds(ctx)
        installed.forEach { id ->
            statusMap[id] = InstallStatus.Installed
        }
        statusMap.keys.toList().forEach { id ->
            if (statusMap[id] is InstallStatus.Installed && id !in installed) {
                statusMap[id] = InstallStatus.NotInstalled
            }
        }
    }

    fun status(versionId: String): InstallStatus =
        statusMap[versionId] ?: InstallStatus.NotInstalled

    fun isInstalled(versionId: String): Boolean =
        status(versionId) is InstallStatus.Installed

    fun installFromUri(versionId: String, uri: Uri, onDone: (Result<VersionStorage.InstalledMeta>) -> Unit = {}) {
        val ctx = appContext
        if (ctx == null) {
            statusMap[versionId] = InstallStatus.Failed("App context missing")
            onDone(Result.failure(IllegalStateException("No context")))
            return
        }
        jobs[versionId]?.cancel()
        statusMap[versionId] = InstallStatus.Installing(0.1f)

        jobs[versionId] = scope.launch {
            statusMap[versionId] = InstallStatus.Installing(0.3f)
            val result = withContext(Dispatchers.IO) {
                VersionStorage.installFromUri(ctx, versionId, uri)
            }
            result.fold(
                onSuccess = {
                    statusMap[versionId] = InstallStatus.Installed
                    onDone(Result.success(it))
                },
                onFailure = { e ->
                    statusMap[versionId] = InstallStatus.Failed(e.message ?: "Install failed")
                    onDone(Result.failure(e))
                }
            )
        }
    }

    fun uninstall(versionId: String) {
        val ctx = appContext ?: return
        jobs[versionId]?.cancel()
        VersionStorage.uninstall(ctx, versionId)
        statusMap[versionId] = InstallStatus.NotInstalled
    }
}
