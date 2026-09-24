package com.premiummcpe.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.premiummcpe.launcher.ui.navigation.PremiumNavGraph
import com.premiummcpe.launcher.ui.navigation.Screen
import com.premiummcpe.launcher.ui.navigation.bottomNavItems
import com.premiummcpe.launcher.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PremiumMCPETheme {
                PremiumMCPEAppRoot()
            }
        }
    }
}

@Composable
fun PremiumMCPEAppRoot() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    Scaffold(
        containerColor = SurfaceDark,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = SurfaceElevated,
                    contentColor = OnSurface,
                    tonalElevation = 8.dp
                ) {
                    bottomNavItems.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(Screen.Home.route) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title
                                )
                            },
                            label = {
                                Text(
                                    text = screen.title,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AccentPrimary,
                                selectedTextColor = AccentPrimary,
                                unselectedIconColor = OnSurfaceMuted,
                                unselectedTextColor = OnSurfaceMuted,
                                indicatorColor = AccentPrimary.copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SurfaceDark)
        ) {
            PremiumNavGraph(navController = navController)
        }
    }
}
