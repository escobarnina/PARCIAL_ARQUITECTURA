package com.asistencia

import com.asistencia.data.AppDatabase
import com.asistencia.routes.usuarioRoutes
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

fun main() {
    println("=".repeat(60))
    println("🚀 Iniciando AsistenciaApp Web Server...")
    println("=".repeat(60))
    
    // Inicializar la base de datos
    println("📦 Inicializando base de datos...")
    val database = AppDatabase()
    println("✅ Base de datos inicializada correctamente")
    
    // Configurar y arrancar el servidor
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = {
        configureApp(database)
    }).start(wait = true)
}

fun Application.configureApp(database: AppDatabase) {
    // Plugin: Logging de llamadas
    install(CallLogging) {
        level = Level.INFO
        format { call ->
            val status = call.response.status()
            val method = call.request.httpMethod.value
            val path = call.request.path()
            val userAgent = call.request.headers["User-Agent"]
            "📡 $status | $method $path | UA: ${userAgent?.take(50) ?: "N/A"}"
        }
    }
    
    // Plugin: Negociación de contenido (JSON)
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    
    // Plugin: CORS (Cross-Origin Resource Sharing)
    install(CORS) {
        anyHost()
        
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Options)
        
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.Accept)
        
        allowCredentials = true
        maxAgeDuration = java.time.Duration.ofDays(1)
    }
    
    // Configuración de rutas
    routing {
        // Ruta raíz - Información del servidor
        get("/") {
            call.respondText(
                """
                🎓 AsistenciaApp Web API
                
                ✅ Estado: Funcionando
                📦 Base de datos: SQLite
                🌐 Puerto: 8080
                
                📚 Endpoints disponibles:
                  - GET  /health          - Estado del servidor
                  - POST /api/login       - Iniciar sesión
                  - GET  /api/usuarios    - Listar usuarios
                  - POST /api/usuarios    - Crear usuario
                
                💡 Ver documentación completa en EJEMPLOS-API.md
                """.trimIndent(),
                ContentType.Text.Plain
            )
        }
        
        // Health check
        get("/health") {
            call.respondText("OK", ContentType.Text.Plain)
        }
        
        // Rutas de usuarios
        usuarioRoutes(database)
    }
    
    // Log de inicio exitoso
    environment.log.info("=".repeat(60))
    environment.log.info("✅ AsistenciaApp Web Server iniciado correctamente")
    environment.log.info("🌐 Servidor disponible en: http://localhost:8080")
    environment.log.info("📡 API disponible en: http://localhost:8080/api")
    environment.log.info("💾 Base de datos: asistenciadb.db")
    environment.log.info("=".repeat(60))
}
