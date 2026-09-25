package com.premiummcpe.launcher.ui.screens.versions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.premiummcpe.launcher.data.auth.AuthState
import com.premiummcpe.launcher.data.download.InstallStatus
import com.premiummcpe.launcher.data.download.VersionInstallManager
import com.premiummcpe.launcher.data.model.VersionCatalog
import com.premiummcpe.launcher.ui.components.*
import com.premiummcpe.launcher.ui.theme.*

@Composable
fun VersionsScreen() {
    var search by remember { mutableStateOf("") }
    var channelFilter by remember { mutableStateOf("All") }
    var showLoginGate by remember { mutableStateOf(false) }
    var playToast by remember { mutableStateOf<String?>(null) }

    val installMap = VersionInstallManager.statusMap
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
            text = "Mojo-style flow for Bedrock: Download → install → Play. Play needs Xbox sign-in. Own the game legally.",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (!signedIn) {
            XboxRequiredBanner(
                onSignInClick = { showLoginGate = true },
                modifier = Modifier.padding(bottom = 12.dp)
            )
        } else {
            AuthState.currentAccount?.let { acc ->
                Text(
                    text = "Signed in as ${acc.gamertag}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SuccessGreen,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }

        playToast?.let { msg ->
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
                val status = installMap[entry.id] ?: InstallStatus.NotInstalled
                MojoVersionRow(
                    title = entry.title,
                    subtitle = "${entry.channel} · ${entry.versionName}",
                    status = status,
                    onDownload = { VersionInstallManager.startDownload(entry.id) },
                    onPlay = {
                        when {
                            !AuthState.isSignedIn -> showLoginGate = true
                            status !is InstallStatus.Installed ->
                                playToast = "Pehle version download / install karo"
                            else ->
                                playToast = "Launch: ${entry.versionName} (native pipeline baad mein)"
                        }
                    },
                    onCancel = { VersionInstallManager.cancel(entry.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Note: Progress UI is Mojo-style. Real Bedrock APK needs official/licensed source or Import — not pirate mirrors.",
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceMuted
                )
                Spacer(modifier = Modifier.height(8.dp))
                SecondaryButton(
                    text = "Import official APK from device",
                    onClick = {
                        filtered.firstOrNull()?.let {
                            VersionInstallManager.markInstalledFromImport(it.id)
                            playToast = "Import: marked installed (file picker next)"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Rounded.Download
                )
            }
        }
    }
}

@Composable
private fun MojoVersionRow(
    title: String,
    subtitle: String,
    status: InstallStatus,
    onDownload: () -> Unit,
    onPlay: () -> Unit,
    onCancel: () -> Unit
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
                    is InstallStatus.Downloading -> {
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { status.progress },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = AccentPrimary,
                            trackColor = SurfaceElevated
                        )
                        Text(
                            text = "Downloading ${(status.progress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelMedium,
                            color = AccentPrimary
                        )
                    }
                    is InstallStatus.Installed ->
                        Text("Installed", style = MaterialTheme.typography.labelMedium, color = SuccessGreen)
                    is InstallStatus.Failed ->
                        Text(status.message, style = MaterialTheme.typography.labelMedium, color = ErrorRed)
                    else ->
                        Text("Not installed", style = MaterialTheme.typography.labelMedium, color = OnSurfaceMuted)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            when (status) {
                is InstallStatus.NotInstalled, is InstallStatus.Failed -> {
                    FilledTonalButton(onClick = onDownload) {
                        Icon(Icons.Rounded.Download, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Download")
                    }
                }
                is InstallStatus.Downloading -> {
                    TextButton(onClick = onCancel) { Text("Cancel", color = OnSurfaceVariant) }
                }
                is InstallStatus.Installed -> {
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
