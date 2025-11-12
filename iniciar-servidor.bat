@echo off
cls
echo ============================================================
echo    AsistenciaApp Web - Iniciando Servidor Ktor
echo ============================================================
echo.
echo [1/3] Compilando proyecto...
echo [2/3] Inicializando base de datos...
echo [3/3] Levantando servidor en puerto 8080...
echo.
echo NOTA: La primera vez puede tomar 1-2 minutos
echo       (descarga de dependencias)
echo.
echo Para DETENER el servidor: Ctrl+C
echo ============================================================
echo.

gradlew.bat -b build-ktor.gradle.kts -c settings-ktor.gradle.kts run

echo.
echo ============================================================
echo Servidor detenido
echo ============================================================
pause
