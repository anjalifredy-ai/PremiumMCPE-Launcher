package com.premiummcpe.launcher.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val PremiumDarkColorScheme = darkColorScheme(
    primary = AccentPrimary,
    onPrimary = Color.White,
    primaryContainer = MinecraftGreenDark,
    onPrimaryContainer = Color.White,
    secondary = AccentSecondary,
    onSecondary = Color.White,
    background = SurfaceDark,
    onBackground = OnSurface,
    surface = SurfaceElevated,
    onSurface = OnSurface,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Color(0xFF3A3A3A),
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun PremiumMCPETheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = PremiumDarkColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = SurfaceDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PremiumTypography,
        content = content
    )
}
