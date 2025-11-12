@echo off
cls
echo ============================================================
echo   AsistenciaApp Web - Servidor Ktor
echo ============================================================
echo.
echo [Paso 1] Activando configuracion WEB...
echo.

REM Activar configuración web
if exist build.gradle.kts (
    move /Y build.gradle.kts build-android.gradle.kts.backup >nul 2>nul
)
if exist settings.gradle.kts (
    move /Y settings.gradle.kts settings-android.gradle.kts.backup >nul 2>nul
)
copy /Y build-ktor.gradle.kts build.gradle.kts >nul
copy /Y settings-ktor.gradle.kts settings.gradle.kts >nul

echo [Paso 2] Iniciando servidor...
echo.
echo NOTA: La primera vez puede tomar 1-2 minutos
echo       (descarga de dependencias)
echo.
echo Para DETENER el servidor: Ctrl+C
echo ============================================================
echo.

REM Ejecutar servidor
gradlew.bat run

echo.
echo ============================================================
echo Servidor detenido
echo.
echo Restaurando configuracion Android...
echo ============================================================

REM Restaurar configuración Android
if exist build.gradle.kts (
    del /Q build.gradle.kts >nul 2>nul
)
if exist settings.gradle.kts (
    del /Q settings.gradle.kts >nul 2>nul
)
if exist build-android.gradle.kts.backup (
    move /Y build-android.gradle.kts.backup build.gradle.kts >nul 2>nul
)
if exist settings-android.gradle.kts.backup (
    move /Y settings-android.gradle.kts.backup settings.gradle.kts >nul 2>nul
)

pause

