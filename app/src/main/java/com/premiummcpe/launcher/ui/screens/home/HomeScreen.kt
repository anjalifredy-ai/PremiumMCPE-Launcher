package com.premiummcpe.launcher.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.premiummcpe.launcher.data.auth.AuthState
import com.premiummcpe.launcher.ui.components.*
import com.premiummcpe.launcher.ui.theme.*

@Composable
fun HomeScreen(
    onNavigateToVersions: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    var showLoginGate by remember { mutableStateOf(false) }
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
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
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
                    text = "Bedrock Launcher",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant
                )
            }
            IconButton(onClick = onNavigateToSettings) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "Settings",
                    tint = OnSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF1E3A1A),
                            Color(0xFF0D1F0B),
                            SurfaceCard
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = if (signedIn) "Ready to play?" else "Sign in to unlock Play",
                    style = MaterialTheme.typography.titleLarge,
                    color = OnSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (signedIn)
                        "Browse the version library and launch after Xbox sign-in.\nOwn Minecraft on Google Play / Microsoft."
                    else
                        "Version list is open. Play stays locked until you sign in with Xbox.\nYou must own Bedrock legally.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant
                )
                Spacer(modifier = Modifier.height(20.dp))
                PrimaryButton(
                    text = if (signedIn) "Open version library" else "Sign in with Microsoft",
                    onClick = {
                        if (signedIn) onNavigateToVersions()
                        else showLoginGate = true
                    },
                    icon = if (signedIn) Icons.Rounded.Download else Icons.Rounded.Lock
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        SectionHeader(title = "Quick Actions")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                icon = Icons.Rounded.Apps,
                title = "Versions",
                subtitle = "Library + Play gate",
                modifier = Modifier.weight(1f),
                onClick = onNavigateToVersions
            )
            QuickActionCard(
                icon = Icons.Rounded.Person,
                title = "Accounts",
                subtitle = "Xbox sign-in",
                modifier = Modifier.weight(1f),
                onClick = { showLoginGate = true }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                icon = Icons.Rounded.Folder,
                title = "Content",
                subtitle = "Worlds & Packs",
                modifier = Modifier.weight(1f),
                onClick = { }
            )
            QuickActionCard(
                icon = Icons.Rounded.Extension,
                title = "Mods",
                subtitle = "Native & external",
                modifier = Modifier.weight(1f),
                onClick = { }
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        SectionHeader(title = "Features")

        FeatureRow(
            icon = Icons.Rounded.Lock,
            title = "Xbox gate",
            description = "No launch until Microsoft / Xbox sign-in in this launcher"
        )
        FeatureRow(
            icon = Icons.Rounded.Apps,
            title = "Version library",
            description = "Browse Release & Preview list anytime"
        )
        FeatureRow(
            icon = Icons.Rounded.Shield,
            title = "Legal ownership",
            description = "Users must own Bedrock on Play / Microsoft Store"
        )
        FeatureRow(
            icon = Icons.Rounded.SwapHoriz,
            title = "Multi account",
            description = "Sign in / out from Accounts tab"
        )

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun QuickActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    PremiumCard(modifier = modifier, onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AccentPrimary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = title, style = MaterialTheme.typography.titleMedium, color = OnSurface)
        Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
    }
}

@Composable
private fun FeatureRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceCard),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = AccentPrimary, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.titleMedium, color = OnSurface)
            Text(text = description, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
        }
    }
}
