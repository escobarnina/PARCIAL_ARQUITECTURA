@echo off
echo ============================================================
echo   Activando configuracion ANDROID
echo ============================================================
echo.

if exist build.gradle.kts (
    echo [1/4] Eliminando build.gradle.kts actual...
    del /Q build.gradle.kts >nul
)

if exist settings.gradle.kts (
    echo [2/4] Eliminando settings.gradle.kts actual...
    del /Q settings.gradle.kts >nul
)

if exist build-android.gradle.kts.backup (
    echo [3/4] Restaurando build-android.gradle.kts...
    move /Y build-android.gradle.kts.backup build.gradle.kts >nul
)

if exist settings-android.gradle.kts.backup (
    echo [4/4] Restaurando settings-android.gradle.kts...
    move /Y settings-android.gradle.kts.backup settings.gradle.kts >nul
)

echo.
echo ============================================================
echo LISTO! Configuracion ANDROID activada
echo.
echo Para volver a WEB, ejecuta: activar-web.bat
echo ============================================================
pause

