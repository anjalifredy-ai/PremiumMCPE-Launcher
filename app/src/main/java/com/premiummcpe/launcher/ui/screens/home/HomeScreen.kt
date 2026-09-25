package com.premiummcpe.launcher.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.premiummcpe.launcher.data.auth.AuthState
import com.premiummcpe.launcher.data.launch.GameLauncher
import com.premiummcpe.launcher.data.model.VersionCatalog
import com.premiummcpe.launcher.ui.components.*
import com.premiummcpe.launcher.ui.theme.*

@Composable
fun HomeScreen(
    onNavigateToVersions: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    var showLoginGate by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf<String?>(null) }
    val signedIn = AuthState.isSignedIn
    val mcInstalled = remember { GameLauncher.isMinecraftInstalled(context) }

    if (showLoginGate) {
        LoginGateDialog(
            onDismiss = { showLoginGate = false },
            onSignedIn = { showLoginGate = false }
        )
    }

    fun tryPlay() {
        if (!AuthState.isSignedIn) {
            showLoginGate = true
            return
        }
        val r = GameLauncher.launchDefaultFromLauncher(context)
        status = r.message
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("PremiumMCPE", style = MaterialTheme.typography.headlineLarge, color = OnSurface, fontWeight = FontWeight.Bold)
                Text("Bedrock launcher", style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant)
            }
            IconButton(onClick = onNavigateToSettings) {
                Icon(Icons.Rounded.Settings, null, tint = OnSurfaceVariant)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFF1B5E20), Color(0xFF0D1F0B), SurfaceCard)))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    if (signedIn) "Play from launcher" else "Sign in to Play",
                    style = MaterialTheme.typography.titleLarge,
                    color = OnSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    when {
                        !signedIn -> "Pehle Microsoft / Xbox login"
                        else -> "Versions mein official APK Install → PLAY (Play Store nahi)"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { tryPlay() },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentPrimary),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(Icons.Filled.PlayArrow, null)
                        Spacer(Modifier.width(6.dp))
                        Text("PLAY", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(onClick = onNavigateToVersions) { Text("Versions") }
                }
                status?.let {
                    Spacer(Modifier = Modifier.height(8.dp))
                    Text(it, color = AccentPrimary, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))
        SectionHeader(title = "Versions", actionText = "All", onAction = onNavigateToVersions)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(VersionCatalog.all.take(12)) { v ->
                PremiumCard(modifier = Modifier.width(140.dp), onClick = onNavigateToVersions) {
                    Text(v.versionName, fontWeight = FontWeight.Bold, color = OnSurface)
                    Text(v.channel, color = AccentPrimary, style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))
        SectionHeader(title = "Flow")
        Text(
            "1) Microsoft login\n2) Versions → Install (apni official APK)\n3) PLAY → usi APK se install/launch\nPlay Store se game download nahi.",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(100.dp))
    }
}
