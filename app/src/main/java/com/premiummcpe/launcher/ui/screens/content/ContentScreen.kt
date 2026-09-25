package com.premiummcpe.launcher.ui.screens.content

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.premiummcpe.launcher.data.content.ContentCatalog
import com.premiummcpe.launcher.data.content.PackItem
import com.premiummcpe.launcher.data.launch.GameLauncher
import com.premiummcpe.launcher.ui.components.*
import com.premiummcpe.launcher.ui.theme.*

@Composable
fun ContentScreen() {
    val context = LocalContext.current
    var tab by remember { mutableStateOf("Resource") }
    var status by remember { mutableStateOf<String?>(null) }

    val packPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) {
            status = "Cancelled"
            return@rememberLauncherForActivityResult
        }
        val r = GameLauncher.openPackWithMinecraft(context, uri)
        status = r.message
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        SectionHeader(title = "Content")
        Text(
            text = "Resource / Behavior / Worlds — Install opens Minecraft for .mcpack / .mcaddon import.",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Resource", "Behavior", "Worlds").forEach { t ->
                FilterChip(
                    selected = tab == t,
                    onClick = { tab = t },
                    label = { Text(t) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AccentPrimary.copy(alpha = 0.25f),
                        selectedLabelColor = AccentPrimary
                    )
                )
            }
        }

        status?.let {
            Text(it, color = AccentPrimary, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        val packs = when (tab) {
            "Behavior" -> ContentCatalog.behaviorPacks
            "Worlds" -> ContentCatalog.worlds
            else -> ContentCatalog.resourcePacks
        }

        Text("Featured", style = MaterialTheme.typography.titleSmall, color = OnSurfaceMuted)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(packs.take(6)) { pack ->
                PremiumCard(modifier = Modifier.width(160.dp)) {
                    Text(pack.name, fontWeight = FontWeight.SemiBold, color = OnSurface, maxLines = 1)
                    Text(pack.type, style = MaterialTheme.typography.labelMedium, color = AccentPrimary)
                    Text(pack.description, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant, maxLines = 2)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("All", style = MaterialTheme.typography.titleSmall, color = OnSurfaceMuted)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(packs, key = { it.id }) { pack ->
                PremiumCard(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            when (pack.type) {
                                "Behavior" -> Icons.Rounded.Inventory2
                                "World" -> Icons.Rounded.Public
                                else -> Icons.Rounded.Folder
                            },
                            null,
                            tint = AccentPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(pack.name, fontWeight = FontWeight.SemiBold, color = OnSurface)
                            Text(pack.description, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                            Text("${pack.author} · ${pack.type}", style = MaterialTheme.typography.labelMedium, color = OnSurfaceMuted)
                        }
                        FilledTonalButton(onClick = {
                            packPicker.launch("*/*")
                            status = "Pick .mcpack / .mcaddon for ${pack.name}"
                        }) { Text("Install") }
                    }
                }
            }
            item {
                SecondaryButton(
                    text = "Import any .mcpack / .mcaddon / .mcworld",
                    onClick = { packPicker.launch("*/*") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
