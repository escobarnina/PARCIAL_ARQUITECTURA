#!/bin/bash

echo "============================================================"
echo "   AsistenciaApp Web - Ejecutar Servidor"
echo "============================================================"
echo ""
echo "Ejecutando servidor en puerto 8080..."
echo ""
echo "Para detener el servidor, presiona Ctrl+C"
echo ""
echo "============================================================"
echo ""

./gradlew -b build-ktor.gradle.kts -c settings-ktor.gradle.kts run

