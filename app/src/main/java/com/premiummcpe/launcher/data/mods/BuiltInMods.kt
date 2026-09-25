package com.premiummcpe.launcher.data.mods

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap

data class ModItem(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val version: String = "1.0"
)

object BuiltInMods {
    val catalog: List<ModItem> = listOf(
        ModItem("auto_sprint", "Auto Sprint", "Hold sprint automatically while moving", "Built-in"),
        ModItem("fps_counter", "FPS Counter", "Show frames per second overlay", "Built-in"),
        ModItem("coords", "Coordinates", "Always-on XYZ coordinates HUD", "Built-in"),
        ModItem("fullbright", "Fullbright", "Increase brightness in dark areas", "Built-in"),
        ModItem("quick_drop", "Quick Drop", "Drop stack with one action", "Built-in"),
        ModItem("zoom", "Zoom", "Hold to zoom FOV", "Built-in"),
        ModItem("hotbar_lock", "Hotbar Lock", "Prevent accidental hotbar switches", "Built-in"),
        ModItem("minimap", "Mini Map", "Lightweight radar-style map (overlay)", "Built-in"),
        ModItem("keystroke", "Keystrokes", "Show movement keys on screen", "Built-in"),
        ModItem("armor_hud", "Armor HUD", "Show armor durability", "Built-in"),
        ModItem("freelook", "Freelook", "Look around without turning body", "Built-in"),
        ModItem("toggle_sprint", "Toggle Sprint", "Tap once to keep sprinting", "Built-in"),
    )

    val enabled: SnapshotStateMap<String, Boolean> = mutableStateMapOf<String, Boolean>().apply {
        catalog.forEach { put(it.id, false) }
    }

    fun toggle(id: String) {
        enabled[id] = !(enabled[id] ?: false)
    }

    fun isEnabled(id: String): Boolean = enabled[id] == true
}
