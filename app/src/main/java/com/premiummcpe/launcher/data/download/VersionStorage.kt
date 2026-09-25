package com.premiummcpe.launcher.data.download

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

object VersionStorage {

    private const val ROOT = "versions"
    private const val APK_NAME = "base.apk"
    private const val META_NAME = "meta.json"

    data class InstalledMeta(
        val versionId: String,
        val packageName: String?,
        val apkVersionName: String?,
        val apkVersionCode: Long?,
        val fileSize: Long,
        val path: String
    )

    fun versionsRoot(context: Context): File =
        File(context.filesDir, ROOT).also { if (!it.exists()) it.mkdirs() }

    fun versionDir(context: Context, versionId: String): File =
        File(versionsRoot(context), sanitize(versionId)).also { if (!it.exists()) it.mkdirs() }

    fun apkFile(context: Context, versionId: String): File =
        File(versionDir(context, versionId), APK_NAME)

    fun isInstalled(context: Context, versionId: String): Boolean {
        val apk = apkFile(context, versionId)
        return apk.exists() && apk.length() > 0L
    }

    fun listInstalledIds(context: Context): Set<String> {
        val root = versionsRoot(context)
        if (!root.exists()) return emptySet()
        return root.listFiles()
            ?.filter { it.isDirectory && File(it, APK_NAME).let { f -> f.exists() && f.length() > 0 } }
            ?.map { it.name }
            ?.toSet()
            ?: emptySet()
    }

    fun installFromUri(context: Context, versionId: String, uri: Uri): Result<InstalledMeta> {
        return try {
            val dir = versionDir(context, versionId)
            val dest = File(dir, APK_NAME)
            val tmp = File(dir, "base.apk.part")
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(tmp).use { output ->
                    input.copyTo(output, bufferSize = 256 * 1024)
                }
            } ?: return Result.failure(IllegalStateException("Cannot open APK stream"))

            if (tmp.length() < 1024) {
                tmp.delete()
                return Result.failure(IllegalStateException("File too small — not a valid APK"))
            }
            if (dest.exists()) dest.delete()
            if (!tmp.renameTo(dest)) {
                tmp.copyTo(dest, overwrite = true)
                tmp.delete()
            }

            val pkgInfo = readPackageInfo(context, dest)
            val meta = InstalledMeta(
                versionId = versionId,
                packageName = pkgInfo?.packageName,
                apkVersionName = pkgInfo?.versionName,
                apkVersionCode = pkgInfo?.longVersionCodeOrCompat(),
                fileSize = dest.length(),
                path = dest.absolutePath
            )
            writeMeta(dir, meta)
            Result.success(meta)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun uninstall(context: Context, versionId: String) {
        val dir = versionDir(context, versionId)
        dir.listFiles()?.forEach { it.delete() }
        dir.delete()
    }

    fun readMeta(context: Context, versionId: String): InstalledMeta? {
        val metaFile = File(versionDir(context, versionId), META_NAME)
        if (!metaFile.exists()) {
            if (!isInstalled(context, versionId)) return null
            val apk = apkFile(context, versionId)
            return InstalledMeta(versionId, null, null, null, apk.length(), apk.absolutePath)
        }
        return try {
            val o = JSONObject(metaFile.readText())
            InstalledMeta(
                versionId = o.optString("versionId", versionId),
                packageName = o.optString("packageName", null),
                apkVersionName = o.optString("apkVersionName", null),
                apkVersionCode = if (o.has("apkVersionCode")) o.getLong("apkVersionCode") else null,
                fileSize = o.optLong("fileSize", 0L),
                path = o.optString("path", apkFile(context, versionId).absolutePath)
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun writeMeta(dir: File, meta: InstalledMeta) {
        val o = JSONObject()
        o.put("versionId", meta.versionId)
        o.put("packageName", meta.packageName ?: "")
        o.put("apkVersionName", meta.apkVersionName ?: "")
        meta.apkVersionCode?.let { o.put("apkVersionCode", it) }
        o.put("fileSize", meta.fileSize)
        o.put("path", meta.path)
        File(dir, META_NAME).writeText(o.toString())
    }

    private fun readPackageInfo(context: Context, apk: File): android.content.pm.PackageInfo? {
        return try {
            val pm = context.packageManager
            if (Build.VERSION.SDK_INT >= 33) {
                pm.getPackageArchiveInfo(apk.absolutePath, PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                pm.getPackageArchiveInfo(apk.absolutePath, 0)
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun android.content.pm.PackageInfo.longVersionCodeOrCompat(): Long {
        return if (Build.VERSION.SDK_INT >= 28) longVersionCode else @Suppress("DEPRECATION") versionCode.toLong()
    }

    private fun sanitize(id: String): String =
        id.replace(Regex("[^a-zA-Z0-9._-]"), "_")
}
