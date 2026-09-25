package com.premiummcpe.launcher.ui.screens.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.premiummcpe.launcher.data.model.ContentTab
import com.premiummcpe.launcher.ui.components.*
import com.premiummcpe.launcher.ui.theme.*

@Composable
fun ContentScreen() {
    var selectedTab by remember { mutableStateOf(ContentTab.WORLDS) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        SectionHeader(title = "Content")

        ScrollableTabRow(
            selectedTabIndex = ContentTab.entries.indexOf(selectedTab),
            containerColor = SurfaceDark,
            contentColor = AccentPrimary,
            edgePadding = 0.dp,
            divider = {}
        ) {
            ContentTab.entries.forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    text = {
                        Text(
                            text = when (tab) {
                                ContentTab.WORLDS -> "Worlds"
                                ContentTab.RESOURCE_PACKS -> "Resources"
                                ContentTab.BEHAVIOR_PACKS -> "Behaviors"
                                ContentTab.SCREENSHOTS -> "Screenshots"
                                ContentTab.SERVERS -> "Servers"
                            },
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    selectedContentColor = AccentPrimary,
                    unselectedContentColor = OnSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            ContentTab.WORLDS -> ContentEmpty(
                icon = Icons.Rounded.Public,
                title = "No worlds yet",
                subtitle = "Worlds from isolated versions will appear here. You can backup, export .mcworld and edit level.dat later."
            )
            ContentTab.RESOURCE_PACKS -> ContentEmpty(
                icon = Icons.Rounded.Palette,
                title = "No resource packs",
                subtitle = "Import .mcpack or folders. Enable/disable per version."
            )
            ContentTab.BEHAVIOR_PACKS -> ContentEmpty(
                icon = Icons.Rounded.Code,
                title = "No behavior packs",
                subtitle = "Manage behavior packs for your isolated installs."
            )
            ContentTab.SCREENSHOTS -> ContentEmpty(
                icon = Icons.Rounded.PhotoCamera,
                title = "No screenshots",
                subtitle = "Screenshots taken in-game will show up here."
            )
            ContentTab.SERVERS -> ContentEmpty(
                icon = Icons.Rounded.Dns,
                title = "No servers",
                subtitle = "Add custom servers or manage featured ones."
            )
        }
    }
}

@Composable
private fun ContentEmpty(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    EmptyState(
        icon = icon,
        title = title,
        subtitle = subtitle,
        actionLabel = "Import / Add",
        onAction = { }
    )
}
