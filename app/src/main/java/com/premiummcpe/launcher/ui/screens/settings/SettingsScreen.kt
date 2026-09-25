package com.premiummcpe.launcher.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.premiummcpe.launcher.ui.theme.*

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = OnSurface
                )
            }
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                color = OnSurface
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            SettingsSection("Appearance") {
                SettingsItem(
                    icon = Icons.Rounded.DarkMode,
                    title = "Theme",
                    subtitle = "Dark (Minecraft style)",
                    onClick = { }
                )
                SettingsItem(
                    icon = Icons.Rounded.Palette,
                    title = "Accent Color",
                    subtitle = "Grass Green",
                    onClick = { }
                )
            }

            SettingsSection("Storage & Paths") {
                SettingsItem(
                    icon = Icons.Rounded.Folder,
                    title = "Content Path",
                    subtitle = "Default isolated workspace",
                    onClick = { }
                )
                SettingsItem(
                    icon = Icons.Rounded.Storage,
                    title = "Clear Cache",
                    subtitle = "Free up temporary files",
                    onClick = { }
                )
            }

            SettingsSection("Game & Launch") {
                SettingsItem(
                    icon = Icons.Rounded.PlayArrow,
                    title = "Launch Options",
                    subtitle = "Foreground service, URI quick launch",
                    onClick = { }
                )
                SettingsItem(
                    icon = Icons.Rounded.Security,
                    title = "Isolation Default",
                    subtitle = "Always isolate new versions",
                    onClick = { }
                )
            }

            SettingsSection("Privacy & About") {
                SettingsItem(
                    icon = Icons.Rounded.PrivacyTip,
                    title = "Privacy",
                    subtitle = "Crash reports & analytics",
                    onClick = { }
                )
                SettingsItem(
                    icon = Icons.Rounded.Info,
                    title = "About",
                    subtitle = "PremiumMCPE v1.0.0-alpha",
                    onClick = { }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = AccentPrimary,
        modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
    )
    Column(content = content)
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = OnSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = OnSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceVariant
            )
        }
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = OnSurfaceMuted
        )
    }
}
