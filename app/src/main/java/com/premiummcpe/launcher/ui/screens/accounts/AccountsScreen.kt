package com.premiummcpe.launcher.ui.screens.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.premiummcpe.launcher.ui.components.*
import com.premiummcpe.launcher.ui.theme.*

@Composable
fun AccountsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        SectionHeader(
            title = "Accounts",
            actionText = "Add",
            onAction = { }
        )

        Text(
            text = "Manage multiple Xbox / Microsoft accounts and switch before launch.",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        EmptyState(
            icon = Icons.Rounded.Person,
            title = "No accounts signed in",
            subtitle = "Sign in with your Microsoft account that owns Minecraft Bedrock. Multiple accounts are supported.",
            actionLabel = "Sign in with Microsoft",
            onAction = { }
        )
    }
}
