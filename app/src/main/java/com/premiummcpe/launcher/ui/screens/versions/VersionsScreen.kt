package com.premiummcpe.launcher.ui.screens.versions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.premiummcpe.launcher.data.auth.AuthState
import com.premiummcpe.launcher.data.model.VersionCatalog
import com.premiummcpe.launcher.ui.components.*
import com.premiummcpe.launcher.ui.theme.*

@Composable
fun VersionsScreen() {
    var search by remember { mutableStateOf("") }
    var channelFilter by remember { mutableStateOf("All") }
    var showLoginGate by remember { mutableStateOf(false) }

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

        SectionHeader(title = "Version library")

        Text(
            text = "Browse all listed Bedrock versions. Play stays locked until Xbox sign-in. Own the game on Google Play / Microsoft.",
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
                    text = "Signed in as ${acc.gamertag} — Play unlocked",
                    style = MaterialTheme.typography.bodySmall,
                    color = SuccessGreen,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search versions…") },
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
                VersionCard(
                    name = entry.title,
                    version = "${entry.channel} · ${entry.versionName}",
                    isSelected = false,
                    lastPlayed = if (entry.isInstalled) "Installed" else "Not installed",
                    onClick = { },
                    onPlay = {
                        if (!AuthState.isSignedIn) {
                            showLoginGate = true
                        }
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                SecondaryButton(
                    text = "Import official APK from device",
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Rounded.Download
                )
            }
        }
    }
}
