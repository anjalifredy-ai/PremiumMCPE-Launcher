package com.premiummcpe.launcher.ui.screens.versions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.premiummcpe.launcher.data.model.GameVersion
import com.premiummcpe.launcher.ui.components.*
import com.premiummcpe.launcher.ui.theme.*

@Composable
fun VersionsScreen() {
    // Demo data – replace with real repository later
    val versions = remember {
        listOf(
            GameVersion(
                id = "1",
                name = "Release Survival",
                versionName = "1.21.50",
                isIsolated = true,
                isInstalled = true,
                isSelected = true
            ),
            GameVersion(
                id = "2",
                name = "Preview Test",
                versionName = "1.21.60.25",
                isIsolated = true,
                isInstalled = true
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        SectionHeader(
            title = "Versions",
            actionText = "Import",
            onAction = { /* TODO: file picker for APK */ }
        )

        Text(
            text = "Isolated installs keep worlds & packs separate.",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (versions.isEmpty()) {
            EmptyState(
                icon = Icons.Rounded.Apps,
                title = "No versions yet",
                subtitle = "Import your official Minecraft APK from Google Play to create an isolated version.",
                actionLabel = "Import APK",
                onAction = { }
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(versions, key = { it.id }) { version ->
                    VersionCard(
                        name = version.name,
                        version = version.versionName,
                        isSelected = version.isSelected,
                        lastPlayed = if (version.isSelected) "Just now" else null,
                        onClick = { /* select version */ },
                        onPlay = { /* launch game */ }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    SecondaryButton(
                        text = "Import Official APK",
                        onClick = { },
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Rounded.Download
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    SecondaryButton(
                        text = "Add Empty Isolated Version",
                        onClick = { },
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Rounded.Add
                    )
                }
            }
        }
    }
}
