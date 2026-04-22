#!/usr/bin/env python3
"""
Run this once on your PC to download gradle-wrapper.jar.
Requires Python 3 and internet access.
"""
import urllib.request, os, sys

jar_url = "https://github.com/gradle/gradle/raw/v5.6.4/gradle/wrapper/gradle-wrapper.jar"
out = os.path.join(os.path.dirname(__file__), "gradle", "wrapper", "gradle-wrapper.jar")
os.makedirs(os.path.dirname(out), exist_ok=True)

print(f"Downloading gradle-wrapper.jar ...")
try:
    urllib.request.urlretrieve(jar_url, out)
    print(f"Saved to {out}")
    print("Done! Now run: bash build_apk.sh   (Linux/Mac) or use GitHub Actions.")
except Exception as e:
    print(f"Failed: {e}")
    print("Try downloading manually from:")
    print("https://github.com/gradle/gradle/raw/v5.6.4/gradle/wrapper/gradle-wrapper.jar")
    print(f"And save it to: gradle/wrapper/gradle-wrapper.jar")
    sys.exit(1)
