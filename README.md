# PremiumMCPE Launcher

**Premium Minecraft Bedrock Edition (MCPE) Launcher for Android**

Inspired by **LeviLaunchroid** + Official Minecraft Launcher UI.

> Clean, modern, high-quality launcher with multi-version isolation, content management, account switching, and mod support foundation.

---

## Features

### UI (Done)
- Premium dark theme with Minecraft grass-green accents
- Jetpack Compose + Material 3
- Smooth cards, bottom navigation, animations
- Home, Versions, Content, Mods, Accounts, Settings screens

### Scaffolded (API / Native to connect later)
- Version isolation UI
- Content hub (Worlds, Resource Packs, Behavior Packs, Screenshots, Servers)
- Multi Xbox account UI
- Mods list (native SO + external catalog ready)
- Settings (theme, paths, launch options, privacy)

### Planned (Levi-style full features)
- Official Minecraft APK import + installation-free launch
- Full data isolation per version
- Xbox / Microsoft authentication
- Native SO / Preloader module loading
- CurseForge / external mod catalog
- World tools (level.dat, .mcworld backup)
- `minecraft://` Quick Launch
- Foreground service

---

## Requirements

- Android 9.0+ (API 28)
- ARM64 recommended
- Legitimate Minecraft Bedrock from Google Play (for full launch)

---

## Build APK

### Option 1: GitHub Actions (recommended)

1. Go to repo **Actions** tab
2. Select **Build APK** workflow
3. Click **Run workflow**
4. After success, download artifact **PremiumMCPE-debug-apk**

### Option 2: Android Studio

```bash
git clone https://github.com/anjalifredy-ai/PremiumMCPE-Launcher.git
cd PremiumMCPE-Launcher
```

Open in Android Studio → Sync Gradle → Build → Build APK(s)

Or terminal:

```bash
./gradlew assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```

---

## Tech Stack

| Layer | Tech |
|-------|------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| Min / Target SDK | 28 / 35 |
| Architecture | Single Activity + screens |

---

## Project Structure

```
app/src/main/java/com/premiummcpe/launcher/
├── MainActivity.kt
├── PremiumMCPEApp.kt
├── data/model/Models.kt
└── ui/
    ├── theme/          # Color, Type, Theme
    ├── components/     # PremiumCard, buttons, VersionCard
    ├── navigation/     # NavGraph + bottom bar
    └── screens/
        ├── home/
        ├── versions/
        ├── content/
        ├── mods/
        ├── accounts/
        └── settings/
```

---

## License

Apache License 2.0

## Credits

- LeviMC / LiteLDev – LeviLaunchroid inspiration
- Official Minecraft design language

**Made for the Bedrock community**

> This is a solid premium UI foundation. Full native launching and mod loading need additional work and a legitimate Minecraft APK.
