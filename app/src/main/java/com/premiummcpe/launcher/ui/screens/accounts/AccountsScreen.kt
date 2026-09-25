package com.premiummcpe.launcher.ui.screens.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.premiummcpe.launcher.data.auth.AuthState
import com.premiummcpe.launcher.ui.components.*
import com.premiummcpe.launcher.ui.theme.*

@Composable
fun AccountsScreen() {
    var showLogin by remember { mutableStateOf(false) }
    val account = AuthState.currentAccount

    if (showLogin) {
        LoginGateDialog(
            onDismiss = { showLogin = false },
            onSignedIn = { showLogin = false }
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
            title = "Accounts",
            actionText = if (account == null) "Sign in" else null,
            onAction = if (account == null) ({ showLogin = true }) else null
        )

        Text(
            text = "Microsoft / Xbox sign-in is required before any version can launch. Use the account that owns Minecraft Bedrock.",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (account == null) {
            EmptyState(
                icon = Icons.Rounded.Person,
                title = "No Xbox account",
                subtitle = "Sign in to unlock Play on the version library. You must own Minecraft on Google Play or Microsoft Store.",
                actionLabel = "Sign in with Microsoft",
                onAction = { showLogin = true }
            )
        } else {
            PremiumCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(AccentPrimary.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.SportsEsports,
                            contentDescription = null,
                            tint = AccentPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = account.gamertag,
                            style = MaterialTheme.typography.titleMedium,
                            color = OnSurface
                        )
                        Text(
                            text = account.email ?: "Microsoft account · Active",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        StatusChip(text = "Play unlocked", isActive = true)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SecondaryButton(
                text = "Sign out",
                onClick = { AuthState.signOut() },
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Rounded.Logout
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "After sign-out, Play is locked again on all versions until you sign in.",
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceMuted
            )
        }
    }
}
