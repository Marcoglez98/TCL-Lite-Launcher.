#!/bin/sh
# Ejecutar en una computadora con adb conectado, NO en la shell del televisor.
set -e
for pkg in com.tcl.esticker com.tcl.appmarket2 com.amazon.amazonvideo.livingroom com.google.android.youtube.tvmusic com.google.android.videos com.tcl.usercenter com.google.android.play.games com.tcl.bi com.tcl.tvweishi; do
  adb shell pm enable --user 0 "$pkg" || true
done
adb shell pm enable --user 0 com.google.android.tvlauncher
printf 'Paquetes solicitados para restaurar. Verifica el estado con adb shell pm list packages -d\n'
