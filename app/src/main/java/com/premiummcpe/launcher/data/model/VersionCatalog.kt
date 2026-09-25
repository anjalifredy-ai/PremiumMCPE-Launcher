package com.premiummcpe.launcher.data.model

/**
 * Catalog of known Bedrock version labels for the version library UI.
 * This is metadata only — not download links to pirated APKs.
 * Real installs come from: user-imported official APK, or licensed sources later.
 */
object VersionCatalog {

    data class CatalogEntry(
        val id: String,
        val versionName: String,
        val channel: String, // Release | Preview
        val title: String,
        val isInstalled: Boolean = false
    )

    val all: List<CatalogEntry> = listOf(
        CatalogEntry("r-1.21.50", "1.21.50", "Release", "Release 1.21.50"),
        CatalogEntry("r-1.21.44", "1.21.44", "Release", "Release 1.21.44"),
        CatalogEntry("r-1.21.31", "1.21.31", "Release", "Release 1.21.31"),
        CatalogEntry("r-1.21.23", "1.21.23", "Release", "Release 1.21.23"),
        CatalogEntry("r-1.21.2", "1.21.2", "Release", "Release 1.21.2"),
        CatalogEntry("r-1.20.81", "1.20.81", "Release", "Release 1.20.81"),
        CatalogEntry("r-1.20.72", "1.20.72", "Release", "Release 1.20.72"),
        CatalogEntry("r-1.20.62", "1.20.62", "Release", "Release 1.20.62"),
        CatalogEntry("p-1.21.60.25", "1.21.60.25", "Preview", "Preview 1.21.60.25"),
        CatalogEntry("p-1.21.50.28", "1.21.50.28", "Preview", "Preview 1.21.50.28"),
        CatalogEntry("p-1.21.40.22", "1.21.40.22", "Preview", "Preview 1.21.40.22"),
    )

    fun toGameVersions(): List<GameVersion> = all.map { e ->
        GameVersion(
            id = e.id,
            name = e.title,
            versionName = e.versionName,
            isIsolated = true,
            isInstalled = e.isInstalled,
            isSelected = false
        )
    }
}
