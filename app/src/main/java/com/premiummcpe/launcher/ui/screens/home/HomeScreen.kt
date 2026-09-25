package com.premiummcpe.launcher.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import com.premiummcpe.launcher.data.auth.AuthState
import com.premiummcpe.launcher.data.launch.GameLauncher
import com.premiummcpe.launcher.data.model.VersionCatalog
import com.premiummcpe.launcher.ui.components.LoginGateDialog
import com.premiummcpe.launcher.ui.components.PremiumCard
import com.premiummcpe.launcher.ui.components.SectionHeader
import com.premiummcpe.launcher.ui.theme.AccentPrimary
import com.premiummcpe.launcher.ui.theme.OnSurface
import com.premiummcpe.launcher.ui.theme.OnSurfaceVariant
import com.premiummcpe.launcher.ui.theme.SurfaceCard
import com.premiummcpe.launcher.ui.theme.SurfaceDark

@Composable
fun HomeScreen(
    onNavigateToVersions: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    var showLoginGate by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf<String?>(null) }
    val signedIn = AuthState.isSignedIn

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
        status = GameLauncher.launchDefaultFromLauncher(context).message
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
                Text(
                    text = "PremiumMCPE",
                    style = MaterialTheme.typography.headlineLarge,
                    color = OnSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Bedrock launcher",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant
                )
            }
            IconButton(onClick = onNavigateToSettings) {
                Icon(Icons.Rounded.Settings, contentDescription = "Settings", tint = OnSurfaceVariant)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF1B5E20), Color(0xFF0D1F0B), SurfaceCard)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = if (signedIn) "Play from launcher" else "Sign in to Play",
                    style = MaterialTheme.typography.titleLarge,
                    color = OnSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (!signedIn) {
                        "Pehle Microsoft / Xbox login"
                    } else {
                        "Versions mein official APK Install, phir PLAY (Play Store nahi)"
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
                        Icon(Icons.Filled.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "PLAY", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(onClick = onNavigateToVersions) {
                        Text("Versions")
                    }
                }
                status?.let { msg ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = msg, color = AccentPrimary, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        SectionHeader(
            title = "Versions",
            actionText = "All",
            onAction = onNavigateToVersions
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(VersionCatalog.all.take(12), key = { it.id }) { v ->
                PremiumCard(
                    modifier = Modifier.width(140.dp),
                    onClick = onNavigateToVersions
                ) {
                    Text(
                        text = v.versionName,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    Text(
                        text = v.channel,
                        color = AccentPrimary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))
        SectionHeader(title = "Flow")
        Text(
            text = "1) Microsoft login\n2) Versions → Install (official APK)\n3) PLAY → usi APK se install/launch\nPlay Store se auto-download nahi.",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(100.dp))
    }
}
