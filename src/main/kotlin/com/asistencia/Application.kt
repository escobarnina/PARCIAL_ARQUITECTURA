package com.asistencia

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.http.*
import com.asistencia.data.AppDatabase
import com.asistencia.routes.usuarioRoutes
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

// Variable global para acceder a la base de datos desde las rutas
lateinit var database: AppDatabase

fun main() {
    println("============================================================")
    println("🚀 Iniciando AsistenciaApp Web Server...")
    println("============================================================")
    
    println("📦 Inicializando base de datos...")
    
    // CORRECCIÓN: Pasar el parámetro dbPath al constructor
    database = AppDatabase(dbPath = "asistenciadb.db")
    
    println("✅ Base de datos inicializada: asistenciadb.db")
    println("🚀 Iniciando servidor Ktor...")
    
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Options)
    }

    install(CallLogging) {
        level = Level.INFO
    }

    routing {
        get("/") {
            call.respondText(
                """
                🎓 AsistenciaApp Web API
                
                ✅ Estado: Funcionando
                📦 Base de datos: SQLite
                🌐 Puerto: 8080
                🏗️ Arquitectura: 3 Capas
                
                Endpoints:
                - GET  /health
                - POST /api/login
                - GET  /api/usuarios
                - POST /api/usuarios
                """.trimIndent(),
                ContentType.Text.Plain
            )
        }

        get("/health") {
            call.respondText("OK", ContentType.Text.Plain)
        }

        usuarioRoutes(database)
    }

    println("============================================================")
    println("✅ Servidor iniciado correctamente")
    println("🌐 Disponible en: http://localhost:8080")
    println("============================================================")
}