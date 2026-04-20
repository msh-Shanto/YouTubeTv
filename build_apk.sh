#!/bin/bash
# ─────────────────────────────────────────────────────────────────
#  build_apk.sh  —  Builds YouTubeTV.apk using Docker
#  No Android Studio needed. Requires: Docker Desktop (or Engine)
# ─────────────────────────────────────────────────────────────────
set -e

echo "🔨  Building YouTubeTV APK via Docker..."

# Pull a ready-made Android SDK image (gradle + SDK 28 already inside)
docker run --rm \
  -v "$(pwd)":/project \
  -w /project \
  mingc/android-build-box:latest \
  bash -c "
    gradle wrapper --gradle-version 5.6.4 2>/dev/null || true
    chmod +x gradlew
    ./gradlew assembleDebug --no-daemon
  "

echo ""
echo "✅  Done!  APK is at:"
echo "    app/build/outputs/apk/debug/app-debug.apk"
echo ""
echo "📺  Copy it to a USB drive, plug into your Android TV,"
echo "    enable 'Install unknown apps' and open the APK."
