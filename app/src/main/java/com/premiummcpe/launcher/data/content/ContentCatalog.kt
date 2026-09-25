package com.premiummcpe.launcher.data.content

data class PackItem(
    val id: String,
    val name: String,
    val type: String,
    val author: String,
    val description: String
)

object ContentCatalog {
    val resourcePacks = listOf(
        PackItem("rp_realistic", "Realistic Textures", "Resource", "Community", "HD-style block textures"),
        PackItem("rp_clear_gui", "Clear GUI", "Resource", "Community", "Cleaner inventory UI"),
        PackItem("rp_shaders_lite", "Shaders Lite", "Resource", "Community", "Soft lighting pack"),
        PackItem("rp_faithful", "Faithful Style", "Resource", "Community", "Classic faithful look"),
        PackItem("rp_lowfire", "Low Fire", "Resource", "Community", "Less distracting fire"),
        PackItem("rp_xray_info", "Ore Highlights", "Resource", "Community", "Visual ore cues (cosmetic)"),
    )
    val behaviorPacks = listOf(
        PackItem("bp_oneplayer", "One Player Sleep", "Behavior", "Community", "Skip night with one sleeper"),
        PackItem("bp_more_mobs", "Mob Tweaks", "Behavior", "Community", "Slight mob AI tweaks"),
        PackItem("bp_stack", "Stack Size+", "Behavior", "Community", "Larger stack sizes"),
        PackItem("bp_coords", "Death Coordinates", "Behavior", "Community", "Show death position"),
        PackItem("bp_fast_leaf", "Fast Leaf Decay", "Behavior", "Community", "Leaves decay faster"),
    )
    val worlds = listOf(
        PackItem("w_skyblock", "Skyblock Starter", "World", "Community", "Classic sky island"),
        PackItem("w_parkour", "Parkour Arena", "World", "Community", "Parkour courses"),
        PackItem("w_survival", "Survival Plus", "World", "Community", "Pre-built survival base"),
    )
}
