#!/bin/sh
set -eu
gradle --no-daemon :app:assembleDebug
printf 'APK: app/build/outputs/apk/debug/app-debug.apk\n'
