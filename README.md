# TCL Lite Launcher

Launcher experimental para TCL UnionTV con Android TV 9 (API 28, armeabi-v7a). Diseñado para usarse con un control remoto y 1.4 GB de RAM.

## Estado
Proyecto fuente inicial. El APK debe compilarse y probarse en el televisor antes de establecerlo como inicio predeterminado. **No deshabilites `com.google.android.tvlauncher`**.

## Compilar desde el celular
Abre la pestaña **Actions** de este repositorio, ejecuta **Build debug APK** con **Run workflow** y descarga el artefacto `tcl-lite-launcher-debug`. Instálalo desde tu celular usando Remote ADB Shell o un instalador de APK para TV.

## Dock
Las seis posiciones se configuran en `app/src/main/res/values/config.xml`. Netflix, YouTube, Play Store, Configuración, Entradas y Multimedia. La opción Entradas abre el selector de fuentes del sistema si el firmware lo admite. Multimedia intenta abrir el centro multimedia TCL.

## Recuperación
Mantén el launcher original habilitado. Para reactivarlo desde ADB: `pm enable --user 0 com.google.android.tvlauncher`. El archivo `scripts/restore.sh` documenta las apps desactivadas durante la limpieza; ejecútalo desde una computadora con adb, no directamente dentro de Remote ADB Shell.

## Limitaciones
No hay acceso ADB directo desde GitHub Actions al televisor. La verificación real de entradas HDMI, botones, captura de pantalla y rendimiento requiere pruebas locales. La instalación del APK no cambia automáticamente el inicio predeterminado.
