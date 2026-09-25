package com.premiummcpe.launcher.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.premiummcpe.launcher.data.auth.AuthState
import com.premiummcpe.launcher.ui.theme.*

@Composable
fun LoginGateDialog(
    onDismiss: () -> Unit,
    onSignedIn: () -> Unit
) {
    var gamertag by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceCard,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(AccentPrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = AccentPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Xbox sign-in required",
                    style = MaterialTheme.typography.titleLarge,
                    color = OnSurface,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Launch Minecraft only after signing in with the Microsoft account that owns Bedrock. Version list stays visible — Play is locked until login.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = gamertag,
                    onValueChange = { gamertag = it },
                    label = { Text("Gamertag (demo)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentPrimary,
                        focusedLabelColor = AccentPrimary,
                        cursorColor = AccentPrimary,
                        focusedTextColor = OnSurface,
                        unfocusedTextColor = OnSurface
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Demo login for UI. Later: real Microsoft OAuth in browser / Custom Tabs.",
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceMuted,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                PrimaryButton(
                    text = if (isLoading) "Signing in…" else "Sign in with Microsoft",
                    onClick = {
                        isLoading = true
                        AuthState.signInDemo(gamertag.ifBlank { "Xbox Player" })
                        isLoading = false
                        onSignedIn()
                    },
                    icon = Icons.Rounded.SportsEsports,
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = OnSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun XboxRequiredBanner(
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = WarningOrange.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Lock,
                contentDescription = null,
                tint = WarningOrange,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Play locked",
                    style = MaterialTheme.typography.titleMedium,
                    color = OnSurface
                )
                Text(
                    text = "Sign in with Xbox to launch any version",
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceVariant
                )
            }
            TextButton(onClick = onSignInClick) {
                Text("Sign in", color = AccentPrimary)
            }
        }
    }
}
