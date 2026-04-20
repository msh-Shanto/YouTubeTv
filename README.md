# 📺 YouTubeTV — WebView App for Old Android TV (5.1.1 / API 22)

A lightweight Android app that wraps **youtube.com/tv** in a full-screen
WebView. Works on Android 5.1.1+ and is optimised for TV remote control.

---

## Features
- Loads `youtube.com/tv` — the official couch-friendly YouTube interface
- Full-screen landscape mode, no title bar
- Desktop User-Agent so YouTube serves the full TV interface  
- Hardware-accelerated WebView for smooth 1080p playback  
- D-pad / remote navigation support (UP/DOWN/LEFT/RIGHT/BACK)  
- Red progress bar while pages load  
- Screen stays on during playback  
- `minSdkVersion 22` → runs on Android 5.1.1

---

## How to Build (Pick ONE method)

### ✅ Method 1 — Docker (Easiest, no Android Studio)

**Requirements:** [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed.

```bash
# 1. Open a terminal in this folder
cd YouTubeTV

# 2. Run the build script
bash build_apk.sh
```

The APK will appear at:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

### ✅ Method 2 — Online Builder (Zero install)

1. Zip the entire `YouTubeTV` folder
2. Go to **https://www.apkonline.net** or **https://buildroid.com**
3. Upload the ZIP → download the compiled APK

---

### ✅ Method 3 — GitHub Actions (Free cloud build)

1. Push this folder to a **GitHub repository**
2. Create `.github/workflows/build.yml`:

```yaml
name: Build APK
on: [push]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '11'
          distribution: 'temurin'
      - name: Build APK
        run: |
          chmod +x gradlew
          ./gradlew assembleDebug
      - uses: actions/upload-artifact@v3
        with:
          name: YouTubeTV-debug
          path: app/build/outputs/apk/debug/app-debug.apk
```

3. After the workflow runs, download the APK from the **Actions → Artifacts** tab.

---

### ✅ Method 4 — Android Studio (if you install it later)

1. Install [Android Studio](https://developer.android.com/studio)
2. Open this folder as a project
3. **Build → Build Bundle(s) / APK(s) → Build APK(s)**
4. APK is in `app/build/outputs/apk/debug/`

---

## Installing on Your Android TV

### Via USB Drive
1. Copy `app-debug.apk` to a FAT32 USB drive
2. Plug into your Android TV
3. Open a file manager app (e.g. **FX File Explorer** from Play Store)
4. Navigate to USB → tap the APK
5. If prompted, enable **"Install unknown apps"** → Install

### Via ADB (Wi-Fi) — No USB needed
```bash
# Find your TV's IP in Settings → About → Network
adb connect 192.168.1.XXX:5555
adb install app/build/outputs/apk/debug/app-debug.apk
```

To enable ADB on the TV:
`Settings → Device Preferences → About → Build number (tap 7 times) → Developer Options → USB Debugging ON`

---

## Remote Control Mapping

| Button        | Action                  |
|---------------|-------------------------|
| BACK          | Go back / exit video    |
| D-pad UP/DOWN | Scroll page             |
| D-pad LEFT/RIGHT | Scroll horizontal    |
| OK / Center   | Click / Select          |
| HOME          | Exit to TV launcher     |

---

## Troubleshooting

| Issue | Fix |
|-------|-----|
| Black screen | Check internet connection on TV |
| "App not installed" | Enable unknown sources in TV settings |
| Video won't play | Make sure WebView is up to date (Settings → Apps → Android System WebView → Update) |
| Login issues | YouTube may require sign-in; use on-screen keyboard |

---

## Project Structure
```
YouTubeTV/
├── app/
│   ├── build.gradle               ← minSdk 22 (Android 5.1)
│   └── src/main/
│       ├── AndroidManifest.xml    ← permissions + TV launcher
│       ├── java/com/oldtv/youtubetv/
│       │   └── MainActivity.java  ← WebView + remote key handling
│       └── res/
│           ├── layout/activity_main.xml
│           ├── values/strings.xml
│           ├── values/styles.xml
│           └── mipmap-hdpi/ic_launcher.png
├── build.gradle
├── settings.gradle
├── gradle.properties
├── gradle/wrapper/gradle-wrapper.properties
├── build_apk.sh                   ← Docker one-command build
└── README.md
```
