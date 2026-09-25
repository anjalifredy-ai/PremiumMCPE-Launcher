package com.premiummcpe.launcher.ui.screens.mods

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Extension
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.premiummcpe.launcher.ui.components.*
import com.premiummcpe.launcher.ui.theme.*

@Composable
fun ModsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        SectionHeader(
            title = "Mods",
            actionText = "Catalog",
            onAction = { }
        )

        Text(
            text = "Native SO modules + external mods (Levi-style foundation).",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        EmptyState(
            icon = Icons.Rounded.Extension,
            title = "No mods installed",
            subtitle = "Import .so / .levipack mods or browse the external catalog. Built-in overlay mods will appear here once native preloader is connected.",
            actionLabel = "Import Mod",
            onAction = { }
        )
    }
}
