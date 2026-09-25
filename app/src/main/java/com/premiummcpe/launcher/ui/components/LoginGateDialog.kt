package com.premiummcpe.launcher.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.premiummcpe.launcher.ui.theme.AccentPrimary
import com.premiummcpe.launcher.ui.theme.OnSurface
import com.premiummcpe.launcher.ui.theme.OnSurfaceVariant
import com.premiummcpe.launcher.ui.theme.SurfaceCard
import com.premiummcpe.launcher.ui.theme.WarningOrange

@Composable
fun LoginGateDialog(
    onDismiss: () -> Unit,
    onSignedIn: () -> Unit
) {
    val context = LocalContext.current
    var gamertag by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(0) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(20.dp), color = SurfaceCard, tonalElevation = 8.dp) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Rounded.Lock,
                    null,
                    tint = AccentPrimary,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(AccentPrimary.copy(alpha = 0.2f))
                        .padding(10.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Microsoft / Xbox Login",
                    style = MaterialTheme.typography.titleLarge,
                    color = OnSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    when (step) {
                        0 -> "Step 1: Microsoft account browser mein open (real login page)."
                        else -> "Step 2: Login ke baad Xbox Gamertag likho → Confirm."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                PrimaryButton(
                    text = "1. Open Microsoft Login",
                    onClick = {
                        MicrosoftAuth.openMicrosoftLogin(context)
                        step = 1
                    },
                    icon = Icons.Rounded.SportsEsports
                )
                Spacer(modifier = Modifier.height(8.dp))
                SecondaryButton(
                    text = "Xbox.com Login",
                    onClick = {
                        MicrosoftAuth.openXboxLogin(context)
                        step = 1
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = gamertag,
                    onValueChange = { gamertag = it },
                    label = { Text("Xbox Gamertag") },
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
                        if (gamertag.isBlank()) return@Button
                        AuthState.completeSignIn(gamertag.trim())
                        onSignedIn()
                    },
                    enabled = gamertag.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentPrimary)
                ) {
                    Text("2. Confirm & Unlock Play", fontWeight = FontWeight.Bold)
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
                Text("Microsoft login required", style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
            }
            TextButton(onClick = onSignInClick) {
                Text("Login", color = AccentPrimary)
            }
        }
    }
}
