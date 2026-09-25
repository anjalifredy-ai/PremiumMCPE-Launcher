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

    val apkPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        val id = pendingInstallId
        pendingInstallId = null
        if (uri == null || id == null) {
            statusMessage = "Install cancelled"
            return@rememberLauncherForActivityResult
        }
        statusMessage = "Installing APK…"
        VersionInstallManager.installFromUri(id, uri) { result ->
            result.fold(
                onSuccess = { meta ->
                    val pkg = meta.packageName ?: "?"
                    val ver = meta.apkVersionName ?: "?"
                    val mb = meta.fileSize / (1024.0 * 1024.0)
                    statusMessage = "Installed: $pkg $ver (%.1f MB)".format(mb)
                },
                onFailure = { e ->
                    statusMessage = "Install failed: ${e.message}"
                }
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
            val matchSearch = search.isBlank() ||
                e.versionName.contains(search, ignoreCase = true) ||
                e.title.contains(search, ignoreCase = true)
            val matchChannel = channelFilter == "All" || e.channel == channelFilter
            matchSearch && matchChannel
        }
    }

    val signedIn = AuthState.isSignedIn

    if (showLoginGate) {
        LoginGateDialog(
            onDismiss = { showLoginGate = false },
            onSignedIn = { showLoginGate = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        SectionHeader(title = "Versions")
        Text(
            text = "Real install: Install → pick official Minecraft APK → saved on device. Play after Xbox sign-in.",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (!signedIn) {
            XboxRequiredBanner(
                onSignInClick = { showLoginGate = true },
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        statusMessage?.let { msg ->
            Text(
                text = msg,
                style = MaterialTheme.typography.bodySmall,
                color = AccentPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search versions…") },
            leadingIcon = { Icon(Icons.Rounded.Search, null, tint = OnSurfaceMuted) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentPrimary,
                focusedTextColor = OnSurface,
                unfocusedTextColor = OnSurface,
                cursorColor = AccentPrimary
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Release", "Preview").forEach { ch ->
                FilterChip(
                    selected = channelFilter == ch,
                    onClick = { channelFilter = ch },
                    label = { Text(ch) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AccentPrimary.copy(alpha = 0.25f),
                        selectedLabelColor = AccentPrimary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(filtered, key = { it.id }) { entry ->
                val status = installMap[entry.id] ?: if (VersionStorage.isInstalled(context, entry.id)) {
                    InstallStatus.Installed
                } else InstallStatus.NotInstalled
                val meta = if (status is InstallStatus.Installed) {
                    VersionStorage.readMeta(context, entry.id)
                } else null

                RealVersionRow(
                    title = entry.title,
                    subtitle = buildString {
                        append(entry.channel)
                        append(" · ")
                        append(entry.versionName)
                        meta?.apkVersionName?.let { append(" · APK $it") }
                        meta?.let { append(" · %.0f MB".format(it.fileSize / (1024.0 * 1024.0))) }
                    },
                    status = status,
                    onInstall = { startRealInstall(entry.id) },
                    onPlay = {
                        when {
                            !AuthState.isSignedIn -> showLoginGate = true
                            status !is InstallStatus.Installed ->
                                statusMessage = "Pehle official APK install karo"
                            else -> {
                                val path = VersionStorage.apkFile(context, entry.id).absolutePath
                                statusMessage = "Ready: $path (launch engine next)"
                            }
                        }
                    },
                    onUninstall = {
                        VersionInstallManager.uninstall(entry.id)
                        statusMessage = "Removed ${entry.versionName}"
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Tip: Official Minecraft APK Install pe choose karo. Har version slot alag folder mein save hota hai.",
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceMuted
                )
            }
        }
    }
}

@Composable
private fun RealVersionRow(
    title: String,
    subtitle: String,
    status: InstallStatus,
    onInstall: () -> Unit,
    onPlay: () -> Unit,
    onUninstall: () -> Unit
) {
    PremiumCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(MinecraftGreenDark, MinecraftGreen))),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.PlayArrow, null, tint = Color.White, modifier = Modifier.size(26.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = OnSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                when (status) {
                    is InstallStatus.Installing -> {
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { status.progress.coerceIn(0.05f, 0.95f) },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = AccentPrimary,
                            trackColor = SurfaceElevated
                        )
                        Text("Installing…", style = MaterialTheme.typography.labelMedium, color = AccentPrimary)
                    }
                    is InstallStatus.Installed ->
                        Text("Installed on device", style = MaterialTheme.typography.labelMedium, color = SuccessGreen)
                    is InstallStatus.Failed ->
                        Text(status.message, style = MaterialTheme.typography.labelMedium, color = ErrorRed)
                    else ->
                        Text("Not installed", style = MaterialTheme.typography.labelMedium, color = OnSurfaceMuted)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            when (status) {
                is InstallStatus.NotInstalled, is InstallStatus.Failed -> {
                    FilledTonalButton(onClick = onInstall) {
                        Icon(Icons.Rounded.Download, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Install")
                    }
                }
                is InstallStatus.Installing -> {
                    CircularProgressIndicator(modifier = Modifier.size(28.dp), strokeWidth = 3.dp, color = AccentPrimary)
                }
                is InstallStatus.Installed -> {
                    IconButton(onClick = onUninstall) {
                        Icon(Icons.Rounded.Delete, "Uninstall", tint = OnSurfaceMuted)
                    }
                    IconButton(
                        onClick = onPlay,
                        modifier = Modifier.size(48.dp).clip(RoundedCornerShape(50)).background(AccentPrimary)
                    ) {
                        Icon(Icons.Filled.PlayArrow, "Play", tint = Color.White)
                    }
                }
            }
        }
    }
}
