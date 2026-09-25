package com.premiummcpe.launcher.ui.screens.versions

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.premiummcpe.launcher.ui.components.LoginGateDialog
import com.premiummcpe.launcher.ui.components.PremiumCard
import com.premiummcpe.launcher.ui.components.SectionHeader
import com.premiummcpe.launcher.ui.components.XboxRequiredBanner
import com.premiummcpe.launcher.ui.theme.AccentPrimary
import com.premiummcpe.launcher.ui.theme.ErrorRed
import com.premiummcpe.launcher.ui.theme.MinecraftGreen
import com.premiummcpe.launcher.ui.theme.MinecraftGreenDark
import com.premiummcpe.launcher.ui.theme.OnSurface
import com.premiummcpe.launcher.ui.theme.OnSurfaceMuted
import com.premiummcpe.launcher.ui.theme.OnSurfaceVariant
import com.premiummcpe.launcher.ui.theme.SuccessGreen
import com.premiummcpe.launcher.ui.theme.SurfaceDark
import com.premiummcpe.launcher.ui.theme.SurfaceElevated

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
        statusMessage = "Saving APK to launcher…"
        VersionInstallManager.installFromUri(id, uri) { result ->
            result.fold(
                onSuccess = { meta ->
                    val mb = meta.fileSize / (1024.0 * 1024.0)
                    statusMessage = "Saved: ${meta.apkVersionName ?: "?"} (%.1f MB)".format(mb)
                },
                onFailure = { e ->
                    statusMessage = "Failed: ${e.message}"
                }
            )
        }
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
            text = "Microsoft login → Install official APK → PLAY (launcher APK, not Play Store)",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (!AuthState.isSignedIn) {
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
            placeholder = { Text("Search…") },
            leadingIcon = {
                Icon(Icons.Rounded.Search, contentDescription = null, tint = OnSurfaceMuted)
            },
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
                val status = installMap[entry.id]
                    ?: if (VersionStorage.isInstalled(context, entry.id)) {
                        InstallStatus.Installed
                    } else {
                        InstallStatus.NotInstalled
                    }

                VersionRow(
                    title = entry.title,
                    subtitle = "${entry.channel} · ${entry.versionName}",
                    status = status,
                    onInstall = {
                        pendingInstallId = entry.id
                        apkPicker.launch("application/vnd.android.package-archive")
                    },
                    onPlay = {
                        if (!AuthState.isSignedIn) {
                            showLoginGate = true
                        } else {
                            statusMessage = GameLauncher.launchVersion(
                                context,
                                entry.id,
                                preview = entry.channel == "Preview"
                            ).message
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
private fun VersionRow(
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
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
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
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceVariant
                )
                when (status) {
                    is InstallStatus.Installing -> {
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { status.progress.coerceIn(0.05f, 0.95f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = AccentPrimary,
                            trackColor = SurfaceElevated
                        )
                        Text(
                            text = "Saving…",
                            style = MaterialTheme.typography.labelMedium,
                            color = AccentPrimary
                        )
                    }
                    is InstallStatus.Installed -> {
                        Text(
                            text = "In launcher",
                            style = MaterialTheme.typography.labelMedium,
                            color = SuccessGreen
                        )
                    }
                    is InstallStatus.Failed -> {
                        Text(
                            text = status.message,
                            style = MaterialTheme.typography.labelMedium,
                            color = ErrorRed
                        )
                    }
                    else -> {
                        Text(
                            text = "APK nahi — Install",
                            style = MaterialTheme.typography.labelMedium,
                            color = OnSurfaceMuted
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            when (status) {
                is InstallStatus.NotInstalled, is InstallStatus.Failed -> {
                    FilledTonalButton(onClick = onInstall) {
                        Icon(
                            imageVector = Icons.Rounded.Download,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Install")
                    }
                    IconButton(
                        onClick = onPlay,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(50))
                            .background(AccentPrimary)
                    ) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = "Play", tint = Color.White)
                    }
                }
                is InstallStatus.Installing -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        strokeWidth = 3.dp,
                        color = AccentPrimary
                    )
                }
                is InstallStatus.Installed -> {
                    IconButton(onClick = onUninstall) {
                        Icon(Icons.Rounded.Delete, contentDescription = "Uninstall", tint = OnSurfaceMuted)
                    }
                    IconButton(
                        onClick = onPlay,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(50))
                            .background(AccentPrimary)
                    ) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = "Play", tint = Color.White)
                    }
                }
            }
        }
    }
}
