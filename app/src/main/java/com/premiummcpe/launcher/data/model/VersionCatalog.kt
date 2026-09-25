package com.premiummcpe.launcher.data.model

object VersionCatalog {

    data class CatalogEntry(
        val id: String,
        val versionName: String,
        val channel: String,
        val title: String,
        val isInstalled: Boolean = false
    )

    val all: List<CatalogEntry> = listOf(
        CatalogEntry("r-1.21.93", "1.21.93", "Release", "Release 1.21.93"),
        CatalogEntry("r-1.21.90", "1.21.90", "Release", "Release 1.21.90"),
        CatalogEntry("r-1.21.80", "1.21.80", "Release", "Release 1.21.80"),
        CatalogEntry("r-1.21.70", "1.21.70", "Release", "Release 1.21.70"),
        CatalogEntry("r-1.21.62", "1.21.62", "Release", "Release 1.21.62"),
        CatalogEntry("r-1.21.50", "1.21.50", "Release", "Release 1.21.50"),
        CatalogEntry("r-1.21.44", "1.21.44", "Release", "Release 1.21.44"),
        CatalogEntry("r-1.21.31", "1.21.31", "Release", "Release 1.21.31"),
        CatalogEntry("r-1.21.23", "1.21.23", "Release", "Release 1.21.23"),
        CatalogEntry("r-1.21.2", "1.21.2", "Release", "Release 1.21.2"),
        CatalogEntry("r-1.21.0", "1.21.0", "Release", "Release 1.21.0"),
        CatalogEntry("r-1.20.81", "1.20.81", "Release", "Release 1.20.81"),
        CatalogEntry("r-1.20.72", "1.20.72", "Release", "Release 1.20.72"),
        CatalogEntry("r-1.20.62", "1.20.62", "Release", "Release 1.20.62"),
        CatalogEntry("r-1.20.50", "1.20.50", "Release", "Release 1.20.50"),
        CatalogEntry("r-1.20.40", "1.20.40", "Release", "Release 1.20.40"),
        CatalogEntry("r-1.20.30", "1.20.30", "Release", "Release 1.20.30"),
        CatalogEntry("r-1.20.15", "1.20.15", "Release", "Release 1.20.15"),
        CatalogEntry("r-1.20.1", "1.20.1", "Release", "Release 1.20.1"),
        CatalogEntry("r-1.19.83", "1.19.83", "Release", "Release 1.19.83"),
        CatalogEntry("r-1.19.73", "1.19.73", "Release", "Release 1.19.73"),
        CatalogEntry("r-1.19.63", "1.19.63", "Release", "Release 1.19.63"),
        CatalogEntry("r-1.19.51", "1.19.51", "Release", "Release 1.19.51"),
        CatalogEntry("r-1.18.33", "1.18.33", "Release", "Release 1.18.33"),
        CatalogEntry("r-1.17.41", "1.17.41", "Release", "Release 1.17.41"),
        CatalogEntry("r-1.16.221", "1.16.221", "Release", "Release 1.16.221"),
        CatalogEntry("p-1.21.100", "1.21.100.x", "Preview", "Preview 1.21.100"),
        CatalogEntry("p-1.21.90", "1.21.90.x", "Preview", "Preview 1.21.90"),
        CatalogEntry("p-1.21.80", "1.21.80.x", "Preview", "Preview 1.21.80"),
        CatalogEntry("p-1.21.70", "1.21.70.x", "Preview", "Preview 1.21.70"),
        CatalogEntry("p-1.21.60.25", "1.21.60.25", "Preview", "Preview 1.21.60.25"),
        CatalogEntry("p-1.21.50.28", "1.21.50.28", "Preview", "Preview 1.21.50.28"),
        CatalogEntry("p-1.21.40.22", "1.21.40.22", "Preview", "Preview 1.21.40.22"),
    )
}
