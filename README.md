# errortune ⚡

> **Cyber & Hacker styled music & video streaming app with offline vault and background playback.**

[![Download APK](https://img.shields.io/badge/Download-APK%20(v1.0.0)-00F0FF?style=for-the-badge&logo=android&logoColor=black)](https://github.com/YOUR_USERNAME/errortune/releases/latest)
[![Platform](https://img.shields.io/badge/Platform-Android%207.0%2B-00FF66.svg?style=flat-square)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF.svg?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4.svg?style=flat-square)](https://developer.android.com/jetpack/compose)
[![Database](https://img.shields.io/badge/Storage-Room%20Offline%20Vault-orange.svg?style=flat-square)](https://developer.android.com/training/data-storage/room)
[![Creator](https://img.shields.io/badge/Created%20By-jd._.___-FF1493.svg?style=flat-square&logo=instagram&logoColor=white)](https://instagram.com/jd._.___)

---

## 📱 Download & Install APK

You can download the pre-built APK directly from the **[GitHub Releases](https://github.com/YOUR_USERNAME/errortune/releases/latest)** section!

### Quick Install Guide:
1. Download **`errortune-v1.0.0.apk`** from [Releases](https://github.com/YOUR_USERNAME/errortune/releases/latest).
2. Open the downloaded `.apk` file on your Android device.
3. If prompted, tap **Settings** and allow **"Install unknown apps"** for your browser / file manager.
4. Tap **Install** and enjoy uninterrupted hacker music and video streaming!

---

## 🎧 Features

- **🌐 Online Streaming & YouTube Integration:**
  - Stream curated cyber lo-fi, synthwave, electronic, chiptune, phonk, and Bollywood/Sargam tracks.
  - Live YouTube search for songs and videos.
  - Dual Mode playback: **Cyber Audio Terminal** with animated neon visualizer & **Video Stream Mode** (YouTube HTML5 player).

- **🔒 100% Offline Vault (No Internet Required):**
  - Download any song or audio stream directly to device storage with one tap.
  - Dedicated **Offline Vault** screen that plays all downloaded tracks seamlessly without cellular data or WiFi.
  - Persistent metadata tracking via Android Room Database.

- **📱 Background & Lockscreen Playback:**
  - Android Foreground Service (`MusicPlaybackService`) with wake-lock support.
  - Full lockscreen media notification controls (Play, Pause, Skip Next, Skip Previous, Seek) powered by Android `MediaSession`.
  - Music keeps playing even when the phone screen is locked or in your pocket.

- **⚡ Zero Authentication Required:**
  - No login, sign-up, or registration barriers. Direct access right into the player terminal.

- **💻 Hacker & Coder Aesthetic:**
  - Neon Cyan (`#00F0FF`), Terminal Green (`#00FF66`), Cyber Purple (`#A855F7`), and Deep Matrix Dark (`#080C14`).
  - Dynamic audio frequency visualizer bars reacting in real time to playback.
  - Monospace typography and hacker status HUD (`SYS_STAT: ONLINE | 320 KBPS`).

- **👤 Creator Profile & Social Link:**
  - Prominent **Created by jd._.___** profile card.
  - Direct touch-to-open Instagram link: [@jd._.___](https://instagram.com/jd._.___).

---

## 🛠️ Tech Stack & Architecture

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material Design 3)
- **Local Persistence:** Room Database + OkHttp File Storage
- **Audio/Video Engine:** Native `android.media.MediaPlayer` + Android `MediaSession` + HTML5 WebView Player
- **Image Loading:** Coil Compose
- **Architecture:** MVVM (Model-View-ViewModel) with Kotlin Coroutines and StateFlow

---

## 🚀 Building From Source

### Prerequisites
- Android Studio Hedgehog (or newer)
- Android SDK 24+ (Android 7.0 or higher)
- JDK 17 or 21

### Build Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/YOUR_USERNAME/errortune.git
   cd errortune
   ```

2. Open the project in **Android Studio**.
3. Let Gradle sync dependencies.
4. Build the APK:
   ```bash
   ./gradlew assembleDebug
   ```
   The APK will be generated at:
   `app/build/outputs/apk/debug/app-debug.apk`

---

## 👨‍💻 Developer & Credits

- **Developer:** jd._.___
- **Instagram Profile:** [@jd._.___](https://instagram.com/jd._.___)
- **App Name:** `errortune`

---

## 📄 License
This project is open-source and available under the [MIT License](LICENSE).
