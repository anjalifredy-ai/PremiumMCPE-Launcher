package com.premiummcpe.launcher.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.premiummcpe.launcher.ui.screens.accounts.AccountsScreen
import com.premiummcpe.launcher.ui.screens.content.ContentScreen
import com.premiummcpe.launcher.ui.screens.home.HomeScreen
import com.premiummcpe.launcher.ui.screens.mods.ModsScreen
import com.premiummcpe.launcher.ui.screens.settings.SettingsScreen
import com.premiummcpe.launcher.ui.screens.versions.VersionsScreen

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : Screen("home", "Home", Icons.Rounded.Home)
    data object Versions : Screen("versions", "Versions", Icons.Rounded.Apps)
    data object Content : Screen("content", "Content", Icons.Rounded.Folder)
    data object Mods : Screen("mods", "Mods", Icons.Rounded.Extension)
    data object Accounts : Screen("accounts", "Accounts", Icons.Rounded.Person)
    data object Settings : Screen("settings", "Settings", Icons.Rounded.Settings)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Versions,
    Screen.Content,
    Screen.Mods,
    Screen.Accounts
)

@Composable
fun PremiumNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToVersions = { navController.navigate(Screen.Versions.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.Versions.route) {
            VersionsScreen()
        }
        composable(Screen.Content.route) {
            ContentScreen()
        }
        composable(Screen.Mods.route) {
            ModsScreen()
        }
        composable(Screen.Accounts.route) {
            AccountsScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
