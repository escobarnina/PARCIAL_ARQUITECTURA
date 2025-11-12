@echo off
echo ============================================================
echo   Activando configuracion WEB (Ktor)
echo ============================================================
echo.

if exist build.gradle.kts (
    echo [1/4] Respaldando build.gradle.kts...
    move /Y build.gradle.kts build-android.gradle.kts.backup >nul
)

if exist settings.gradle.kts (
    echo [2/4] Respaldando settings.gradle.kts...
    move /Y settings.gradle.kts settings-android.gradle.kts.backup >nul
)

echo [3/4] Activando build-ktor.gradle.kts...
copy /Y build-ktor.gradle.kts build.gradle.kts >nul

echo [4/4] Activando settings-ktor.gradle.kts...
copy /Y settings-ktor.gradle.kts settings.gradle.kts >nul

echo.
echo ============================================================
echo LISTO! Configuracion WEB activada
echo.
echo Ahora puedes ejecutar:
echo   gradlew.bat run
echo.
echo Para volver a Android, ejecuta: activar-android.bat
echo ============================================================
pause

