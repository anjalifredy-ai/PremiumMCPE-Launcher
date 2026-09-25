package com.premiummcpe.launcher.ui.screens.mods

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Extension
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.premiummcpe.launcher.data.mods.BuiltInMods
import com.premiummcpe.launcher.ui.components.*
import com.premiummcpe.launcher.ui.theme.*

@Composable
fun ModsScreen() {
    var filter by remember { mutableStateOf("All") }
    val enabledMap = BuiltInMods.enabled
    val list = remember(filter, enabledMap.toMap()) {
        when (filter) {
            "Enabled" -> BuiltInMods.catalog.filter { enabledMap[it.id] == true }
            "Built-in" -> BuiltInMods.catalog.filter { it.category == "Built-in" }
            else -> BuiltInMods.catalog
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        SectionHeader(title = "Mods")
        Text(
            text = "Levi-style modules. Toggle on/off. Native SO injection needs Preloader (advanced).",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "Built-in", "Enabled").forEach { ch ->
                FilterChip(
                    selected = filter == ch,
                    onClick = { filter = ch },
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(list, key = { it.id }) { mod ->
                val on = enabledMap[mod.id] == true
                PremiumCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Rounded.Extension,
                            null,
                            tint = if (on) AccentPrimary else OnSurfaceMuted,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(mod.name, style = MaterialTheme.typography.titleMedium, color = OnSurface, fontWeight = FontWeight.SemiBold)
                            Text(mod.description, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                            Text(mod.category, style = MaterialTheme.typography.labelMedium, color = OnSurfaceMuted)
                        }
                        Switch(
                            checked = on,
                            onCheckedChange = { BuiltInMods.toggle(mod.id) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = OnSurface,
                                checkedTrackColor = AccentPrimary
                            )
                        )
                    }
                }
            }
        }
    }
}
