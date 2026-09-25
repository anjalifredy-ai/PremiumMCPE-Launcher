package com.premiummcpe.launcher.ui.screens.versions

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.premiummcpe.launcher.data.auth.AuthState
import com.premiummcpe.launcher.data.download.InstallStatus
import com.premiummcpe.launcher.data.download.VersionInstallManager
import com.premiummcpe.launcher.data.download.VersionStorage
import com.premiummcpe.launcher.data.launch.GameLauncher
import com.premiummcpe.launcher.data.model.VersionCatalog
import com.premiummcpe.launcher.ui.components.*
import com.premiummcpe.launcher.ui.theme.*

@Composable
fun VersionsScreen() {
    val context = LocalContext.current
    var search by remember { mutableStateOf("") }
    var channelFilter by remember { mutableStateOf("All") }
    var showLoginGate by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var pendingInstallId by remember { mutableStateOf<String?>(null) }
    val installMap = VersionInstallManager.statusMap

    LaunchedEffect(Unit) {
        VersionInstallManager.init(context)
        VersionInstallManager.rescan()
    }

    val apkPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val id = pendingInstallId
        pendingInstallId = null
        if (uri == null || id == null) {
            statusMessage = "Install cancelled"
            return@rememberLauncherForActivityResult
        }
        statusMessage = "Saving APK to launcher…"
        VersionInstallManager.installFromUri(id, uri) { result ->
            result.fold(
                onSuccess = { meta ->
                    val mb = meta.fileSize / (1024.0 * 1024.0)
                    statusMessage = "Saved in launcher: ${meta.packageName ?: "?"} ${meta.apkVersionName ?: "?"} (%.1f MB)".format(mb)
                },
                onFailure = { e -> statusMessage = "Failed: ${e.message}" }
            )
        }
    }

    fun startRealInstall(versionId: String) {
        pendingInstallId = versionId
        apkPicker.launch("application/vnd.android.package-archive")
    }

    val catalog = remember { VersionCatalog.all }
    val filtered = remember(search, channelFilter) {
        catalog.filter { e ->
            val matchSearch = search.isBlank() || e.versionName.contains(search, true) || e.title.contains(search, true)
            val matchChannel = channelFilter == "All" || e.channel == channelFilter
            matchSearch && matchChannel
        }
    }

    if (showLoginGate) {
        LoginGateDialog(onDismiss = { showLoginGate = false }, onSignedIn = { showLoginGate = false })
    }

    Column(Modifier = Modifier.fillMaxSize().background(SurfaceDark).padding(horizontal = 20.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        SectionHeader(title = "Versions")
        Text(
            "1) Microsoft login  2) Install = official APK is slot mein save  3) PLAY = usi APK se install/launch (Play Store nahi)",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        if (!AuthState.isSignedIn) {
            XboxRequiredBanner(onSignInClick = { showLoginGate = true }, modifier = Modifier.padding(bottom = 12.dp))
        }
        statusMessage?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, color = AccentPrimary, modifier = Modifier.padding(bottom = 8.dp))
        }
        OutlinedTextField(
            value = search, onValueChange = { search = it }, modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search…") },
            leadingIcon = { Icon(Icons.Rounded.Search, null, tint = OnSurfaceMuted) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentPrimary, focusedTextColor = OnSurface,
                unfocusedTextColor = OnSurface, cursorColor = AccentPrimary
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Release", "Preview").forEach { ch ->
                FilterChip(
                    selected = channelFilter == ch, onClick = { channelFilter = ch }, label = { Text(ch) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AccentPrimary.copy(alpha = 0.25f),
                        selectedLabelColor = AccentPrimary
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 100.dp)) {
            items(filtered, key = { it.id }) { entry ->
                val status = installMap[entry.id] ?: if (VersionStorage.isInstalled(context, entry.id)) InstallStatus.Installed else InstallStatus.NotInstalled
                RealVersionRow(
                    title = entry.title,
                    subtitle = "${entry.channel} · ${entry.versionName}",
                    status = status,
                    onInstall = { startRealInstall(entry.id) },
                    onPlay = {
                        if (!AuthState.isSignedIn) showLoginGate = true
                        else {
                            val r = GameLauncher.launchVersion(context, entry.id, preview = entry.channel == "Preview")
                            statusMessage = r.message
                        }
                    },
                    onUninstall = {
                        VersionInstallManager.uninstall(entry.id)
                        statusMessage = "Removed ${entry.versionName}"
                    }
                )
            }
        }
    }
}

@Composable
private fun RealVersionRow(
    title: String, subtitle: String, status: InstallStatus,
    onInstall: () -> Unit, onPlay: () -> Unit, onUninstall: () -> Unit
) {
    PremiumCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(MinecraftGreenDark, MinecraftGreen))),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.PlayArrow, null, tint = Color.White, modifier = Modifier.size(26.dp)) }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = OnSurface, maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                when (status) {
                    is InstallStatus.Installing -> Text("Saving…", color = AccentPrimary, style = MaterialTheme.typography.labelMedium)
                    is InstallStatus.Installed -> Text("In launcher", color = SuccessGreen, style = MaterialTheme.typography.labelMedium)
                    is InstallStatus.Failed -> Text(status.message, color = ErrorRed, style = MaterialTheme.typography.labelMedium)
                    else -> Text("APK nahi — Install karo", color = OnSurfaceMuted, style = MaterialTheme.typography.labelMedium)
                }
            }
            when (status) {
                is InstallStatus.NotInstalled, is InstallStatus.Failed -> {
                    FilledTonalButton(onClick = onInstall) {
                        Icon(Icons.Rounded.Download, null, Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text("Install")
                    }
                    IconButton(onClick = onPlay, modifier = Modifier.size(44.dp).clip(RoundedCornerShape(50)).background(AccentPrimary)) {
                        Icon(Icons.Filled.PlayArrow, "Play", tint = Color.White)
                    }
                }
                is InstallStatus.Installing -> CircularProgressIndicator(Modifier = Modifier.size(28.dp), strokeWidth = 3.dp, color = AccentPrimary)
                is InstallStatus.Installed -> {
                    IconButton(onClick = onUninstall) { Icon(Icons.Rounded.Delete, null, tint = OnSurfaceMuted) }
                    IconButton(onClick = onPlay, modifier = Modifier.size(48.dp).clip(RoundedCornerShape(50)).background(AccentPrimary)) {
                        Icon(Icons.Filled.PlayArrow, "Play", tint = Color.White)
                    }
                }
            }
        }
    }
}
