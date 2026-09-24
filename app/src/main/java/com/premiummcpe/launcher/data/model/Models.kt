package com.premiummcpe.launcher.data.model

data class GameVersion(
    val id: String,
    val name: String,
    val versionName: String,          // e.g. "1.21.50"
    val isIsolated: Boolean = true,
    val isInstalled: Boolean = false,
    val lastPlayed: Long? = null,
    val iconPath: String? = null,
    val path: String? = null,
    val isSelected: Boolean = false
)

data class WorldItem(
    val id: String,
    val name: String,
    val lastPlayed: Long? = null,
    val sizeBytes: Long = 0,
    val gameMode: String = "Survival",
    val iconPath: String? = null
)

data class ResourcePackItem(
    val id: String,
    val name: String,
    val description: String = "",
    val version: String = "",
    val isEnabled: Boolean = false,
    val sizeBytes: Long = 0
)

data class ServerItem(
    val id: String,
    val name: String,
    val address: String,
    val port: Int = 19132,
    val isOnline: Boolean? = null
)

data class XboxAccount(
    val id: String,
    val gamertag: String,
    val email: String? = null,
    val avatarUrl: String? = null,
    val isActive: Boolean = false
)

data class ModItem(
    val id: String,
    val name: String,
    val author: String = "",
    val version: String = "",
    val description: String = "",
    val isEnabled: Boolean = false,
    val isBuiltIn: Boolean = false
)

enum class ContentTab {
    WORLDS, RESOURCE_PACKS, BEHAVIOR_PACKS, SCREENSHOTS, SERVERS
}
