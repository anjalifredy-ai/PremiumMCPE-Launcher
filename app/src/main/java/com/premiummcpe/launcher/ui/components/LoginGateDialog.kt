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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.premiummcpe.launcher.data.auth.AuthState
import com.premiummcpe.launcher.data.auth.MicrosoftAuth
import com.premiummcpe.launcher.ui.theme.*

@Composable
fun LoginGateDialog(
    onDismiss: () -> Unit,
    onSignedIn: () -> Unit
) {
    val context = LocalContext.current
    var gamertag by remember { mutableStateOf("") }
    var openedBrowser by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceCard,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(AccentPrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Lock, null, tint = AccentPrimary, modifier = Modifier.size(32.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Microsoft / Xbox sign-in",
                    style = MaterialTheme.typography.titleLarge,
                    color = OnSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "1) Open Microsoft login (real browser)\n2) Sign in with account that owns Minecraft\n3) Enter Gamertag to unlock Play",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                PrimaryButton(
                    text = if (openedBrowser) "Browser opened — sign in there" else "Open Microsoft Login",
                    onClick = {
                        MicrosoftAuth.openMicrosoftLogin(context)
                        openedBrowser = true
                    },
                    icon = Icons.Rounded.SportsEsports
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = gamertag,
                    onValueChange = { gamertag = it },
                    label = { Text("Your Gamertag") },
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
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        AuthState.completeSignIn(gamertag.ifBlank { "Xbox Player" })
                        onSignedIn()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentPrimary)
                ) {
                    Text("Confirm & Unlock Play", fontWeight = FontWeight.Bold)
                }
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
            Icon(Icons.Rounded.Lock, null, tint = WarningOrange, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Play locked", style = MaterialTheme.typography.titleMedium, color = OnSurface)
                Text("Sign in with Microsoft / Xbox", style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
            }
            TextButton(onClick = onSignInClick) {
                Text("Sign in", color = AccentPrimary)
            }
        }
    }
}
