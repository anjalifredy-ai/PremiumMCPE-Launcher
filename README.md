# PremiumMCPE Launcher

**Premium Minecraft Bedrock Edition (MCPE) Launcher for Android**

Inspired by **LeviLaunchroid** + Official Minecraft Launcher UI aesthetics.

> Clean, modern, high-quality launcher with multi-version isolation, content management, account switching, and mod support foundation.

---

## Features (Current Scaffold + Roadmap)

### Implemented / Scaffolded
- **Premium Official-style UI** (Jetpack Compose + Material 3)
  - Dark theme with Minecraft grass-green accents
  - Smooth animations, glassmorphism cards, bottom navigation
- **Home Dashboard** – Quick launch, recent versions, news placeholder
- **Version Manager** – Isolated versions list, add/import APK, rename, delete
- **Content Hub**
  - Worlds management (list / backup / export .mcworld)
  - Resource Packs & Behavior Packs
  - Screenshots
  - Servers list
- **Accounts** – Multiple Xbox / Microsoft account UI (auth placeholder)
- **Mods** – External mods list + built-in mods placeholder
- **Settings** – Theme, language, paths, privacy, about
- **Modern Architecture** – Clean MVVM + Navigation + Room ready

### Planned / To Implement (API & Native)
- Official Minecraft APK import + installation-free launch
- Full version isolation (separate data directories)
- Xbox Live / Microsoft account authentication
- Native SO / Preloader module loading (Levi-style)
- CurseForge / external mod catalog
- World tools (level.dat editor, backup)
- Quick Launch via `minecraft://` URI
- Foreground service for background stability
- Gyroscope / overlay mods foundation

---

## Requirements

- Android 9.0+ (API 28)
- ARM64 device recommended
- Legitimate Minecraft Bedrock Edition from Google Play (required for full launch)

---

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM + Single Activity
- **Navigation**: Navigation Compose
- **DI**: ready for Hilt / Koin
- **Storage**: Room + DataStore (ready)
- **Images**: Coil
- **Min SDK**: 28 | Target SDK: 35

---

## Project Structure

```
app/
├── src/main/java/com/premiummcpe/launcher/
│   ├── MainActivity.kt
│   ├── PremiumMCPEApp.kt
│   ├── ui/
│   │   ├── theme/          # Colors, Typography, Theme
│   │   ├── components/     # Reusable premium components
│   │   ├── screens/        # Home, Versions, Content, Mods, Accounts, Settings
│   │   └── navigation/
│   ├── data/               # Models, Repository stubs
│   ├── viewmodel/
│   └── util/
├── res/
└── AndroidManifest.xml
```

---

## Getting Started

1. Clone the repo
2. Open in **Android Studio** (latest recommended)
3. Sync Gradle
4. Run on device / emulator (API 28+)

```bash
git clone https://github.com/anjalifredy-ai/PremiumMCPE-Launcher.git
```

---

## License

Apache License 2.0 (same spirit as LeviLaunchroid)

---

## Credits & Inspiration

- LeviMC / LiteLDev – LeviLaunchroid
- Official Minecraft Launcher design language
- Minecraft community

**Made with ❤️ for the Bedrock community**

> Note: This is a high-quality foundation. Full native launching, account auth, and mod loading require additional native work and legitimate Minecraft APK. API integration will be added later as planned.
